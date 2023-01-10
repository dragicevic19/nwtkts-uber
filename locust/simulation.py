import requests
import json

from locust import HttpUser, task, between, events
from random import randrange


start_and_end_points = [
    (45.235866, 19.807387),     # Djordja Mikeša 2
    (45.247309, 19.796717),     # Andje Rankovic 2
    (45.259711, 19.809787),     # Veselina Maslese 62
    (45.261421, 19.823026),     # Jovana Hranilovica 2
    (45.265435, 19.847805),     # Bele njive 24
    (45.255521, 19.845071),     # Njegoseva 2
    (45.249241, 19.852152),     # Stevana Musica 20
    (45.242509, 19.844632),     # Boska Buhe 10A
    (45.254366, 19.861088),     # Strosmajerova 2
    (45.223481, 19.847990)      # Gajeva 2
]


taxi_stops = [
    (45.238548, 19.848225),   # Stajaliste na keju
    (45.243097, 19.836284),   # Stajaliste kod limanske pijace
    (45.256863, 19.844129),   # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),   # Stajaliste na telepu
    (45.246540, 19.849282)    # Stajaliste kod velike menze
]


license_plates = [
    'NS-001-AA',
    'NS-001-AB',
    'NS-001-AC'
]


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    requests.delete('http://localhost:8080/api/ride')
    requests.delete('http://localhost:8080/api/vehicle')


class QuickstartUser(HttpUser):
    host = 'http://localhost:8080'
    wait_time = between(0.5, 2)

    def on_start(self):
        random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
        self.vehicle = self.client.post('/api/vehicle', json={
            'licensePlateNumber': license_plates.pop(0),
            'latitude': random_taxi_stop[0],
            'longitude': random_taxi_stop[1]
        }).json()
        self.driving_to_start_point = True
        self.driving_the_route = False
        self.driving_to_taxi_stop = False
        self.departure = random_taxi_stop
        self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
        self.get_new_coordinates()

    @task
    def update_vehicle_coordinates(self):
        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            self.client.put(f"/api/vehicle/{self.vehicle['id']}", json={
                'latitude': new_coordinate[0],
                'longitude': new_coordinate[1]
            })
        elif len(self.coordinates) == 0 and self.driving_to_start_point:
            self.end_ride()
            self.departure = self.destination
            while (self.departure[0] == self.destination[0]):
                self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
            self.get_new_coordinates()
            self.driving_to_start_point = False
            self.driving_the_route = True
        elif len(self.coordinates) == 0 and self.driving_the_route:
            random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
            start_and_end_points.append(self.departure)
            self.end_ride()
            self.departure = self.destination
            self.destination = random_taxi_stop
            self.get_new_coordinates()
            self.driving_the_route = False
            self.driving_to_taxi_stop = True
        elif len(self.coordinates) == 0 and self.driving_to_taxi_stop:
            random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
            start_and_end_points.append(self.departure)
            self.end_ride()
            self.departure = random_taxi_stop
            self.destination = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
            self.get_new_coordinates()
            self.driving_to_taxi_stop = False
            self.driving_to_start_point = True

    def get_new_coordinates(self):
        response = requests.get(f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
        self.routeGeoJSON = response.json()
        self.coordinates = []
        for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]
        self.ride = self.client.post('/api/ride', json={
            'routeJSON': json.dumps(self.routeGeoJSON),
            'rideStatus': 0,
            'vehicle': {
                'id': self.vehicle['id'],
                'licensePlateNumber': self.vehicle['licensePlateNumber'],
                'latitude': self.coordinates[0][0],
                'longitude': self.coordinates[0][1]
            } 
        }).json()

    def end_ride(self):
        self.client.put(f"/api/ride/{self.ride['id']}")
