import random
import requests
import json
import schedule
import time

from locust import HttpUser, TaskSet, task, between, events
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

# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
    # requests.delete('http://localhost:8080/api/ride')
    # requests.delete('http://localhost:8080/api/vehicle')


class QuickstartUser(HttpUser):
    host = 'http://localhost:8080'
    #wait_time = between(0.5, 2)

    def on_start(self):
        self.active_drivers = {} # dictionary to keep track of active drivers and their corresponding schedule jobs
        self.scheduled_jobs = {}
        schedule.every(5).seconds.do(self.check_active_drivers)
        while True:
            schedule.run_pending()
            time.sleep(1)

    def check_active_drivers(self):
        current_active_drivers = self.get_active_drivers()

        # check for new active drivers
        new_active_drivers = self.check_for_new_drivers(current_active_drivers)
        for driver in new_active_drivers:
            job = schedule.every(1).seconds.do(self.update_location, driver)
            self.scheduled_jobs[driver['id']] = job

        # check for inactive drivers
        inactive_drivers = self.check_for_inactive_drivers(current_active_drivers)
        for driver in inactive_drivers:
            schedule.cancel_job(self.scheduled_jobs[driver['id']])
            del self.scheduled_jobs[driver['id']]
            self.end_ride(driver)
            del self.active_drivers[driver['id']]
        
    def check_for_new_drivers(self, current_active_drivers):
        new_active = []
        for driver in current_active_drivers:
            if not driver['id'] in self.active_drivers:
                self.active_drivers[driver['id']] = driver
                new_active.append(driver)
            else:
                if self.active_drivers[driver['id']]['available'] and not driver['available']:
                    self.end_fake_ride(self.active_drivers[driver['id']])       # zavrsavam ovo sto se bezveze vozao
                    self.active_drivers[driver['id']] = driver
                    job = schedule.every(1).second.do(self.update_location, self.active_drivers[driver['id']])
                    self.scheduled_jobs[driver['id']] = job

        return new_active

    def check_for_inactive_drivers(self, current_active_drivers):
        inactive = []
        for driverId in self.active_drivers.keys():
            found = False
            for driver in current_active_drivers:
                if driver['id'] == driverId:
                    found = True
                    break
            if not found:
                inactive.append(self.active_drivers[driverId])
        return inactive

    def get_active_drivers(self):
        active_drivers = self.client.get("/api/driver/active").json()
        return active_drivers

    def update_location(self, driver):
        if driver['available'] and not 'isInit' in driver:  # random lokacije
            driver['isInit'] = True
            driver['driving_to_start_point'] = True
            driver['driving_the_route'] = False
            driver['driving_to_taxi_stop'] = False
            driver['departure'] = [driver['driversLatitude'], driver['driversLongitude']]
            driver['destination'] = start_and_end_points.pop(randrange(0, len(start_and_end_points)))
            self.get_new_coordinates(driver)
            self.update_vehicle_coordinates(driver)

        elif driver['available'] and driver['isInit']:
            self.update_vehicle_coordinates(driver)

        else:
            if not 'hasRide' in driver:                     # pravim zatrazenu voznju
                driver['hasRide'] = True
                driver['driving_to_start_point'] = True
                driver['driving_the_route'] = False
                self.makeRouteDriverToPickup(driver)
                self.update_vehicle_coordinates(driver)
            else:                                           # update location
                self.update_vehicle_coordinates(driver)

    def makeRouteDriverToPickup(self, driver):
        response = requests.get('https://routing.openstreetmap.de/routed-car/route/v1/driving/' + str(driver['driversLongitude']) +',' + str(driver['driversLatitude']) + ';' + str(driver['pickupLocationLongitude']) +',' + str(driver['pickupLocationLatitude']) + '?geometries=geojson&overview=false&alternatives=true&steps=true')
        routeGeoJSON = response.json()
        driver['coordinates'] = []

        driverToPickupRoute = routeGeoJSON['routes'][0]['legs'][0]
        driver['usersRoute'] = json.loads(driver['usersRoute'])
        driver['usersRoute'].insert(0, driverToPickupRoute) # set route to pickup first

        for leg in driver['usersRoute']:
            for step in leg['steps']:
                driver['coordinates'] = [*driver['coordinates'], *step['geometry']['coordinates']]

        driver['ride'] = self.client.get('/api/ride/driver/' + str(driver['id'])).json()
        


    def get_new_coordinates(self, driver):
        response = requests.get('https://routing.openstreetmap.de/routed-car/route/v1/driving/' + str(driver['departure'][1]) +',' + str(driver['departure'][0]) + ';' + str(driver['destination'][1]) +',' + str(driver['destination'][0]) + '?geometries=geojson&overview=false&alternatives=true&steps=true')
        routeGeoJSON = response.json()
        legs = routeGeoJSON['routes'][0]['legs']

        driver['coordinates'] = []

        for step in legs[0]['steps']:
            driver['coordinates'] = [*driver['coordinates'], *step['geometry']['coordinates']]

        driver['ride'] = self.client.post('/api/ride', json={
            'routeJSON': json.dumps(legs),
            'rideStatus': 0,
            'driverId': driver['id'],
            'vehicleLatitude': driver['coordinates'][0][1],
            'vehicleLongitude': driver['coordinates'][0][0]
            } 
        ).json()

    def update_vehicle_coordinates(self, driver):

        if len(driver['coordinates']) > 0:
            new_coordinate = driver['coordinates'].pop(0)

            self.client.put(f"/api/vehicle/{driver['vehicleId']}", json={
                'latitude': new_coordinate[1],
                'longitude': new_coordinate[0]
            })

        elif 'hasRide' in driver and driver['hasRide']:
            del driver['hasRide']
            self.end_ride(driver)

        else:
            self.end_ride(driver)

    def end_ride(self, driver):
        schedule.cancel_job(self.scheduled_jobs[driver['id']])
        del self.active_drivers[driver['id']]
        del self.scheduled_jobs[driver['id']]
        self.client.put(f"/api/ride/{driver['ride']['id']}")

    def end_fake_ride(self, driver):
        schedule.cancel_job(self.scheduled_jobs[driver['id']])
        del self.active_drivers[driver['id']]
        del self.scheduled_jobs[driver['id']]
        self.client.put(f"/api/ride/fake/{driver['ride']['id']}")