import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LatLngExpression } from 'leaflet';
import { Observable, Subject } from 'rxjs';
import { Coordinates, CoordType } from '../models/Coordinates';
import { Ride } from '../models/Ride';
import { Route } from '../models/Route';

@Injectable({
  providedIn: 'root',
})
export class MapService {

  pickupCoords: any;
  destinationCoords: any;

  coordsChange: Subject<Coordinates> = new Subject<Coordinates>();
  routeChange: Subject<Route | null> = new Subject<Route | null>();

  constructor(private http: HttpClient) {
    this.pickupCoords = [];
    this.destinationCoords = [];
  }

  changePickup(coords: Array<number>) {
    this.pickupCoords = coords;
    this.coordsChange.next(
      new Coordinates(this.pickupCoords, CoordType.PICKUP)
    );
  }

  changeDestination(coords: Array<number>) {
    this.destinationCoords = coords;
    this.coordsChange.next(
      new Coordinates(this.destinationCoords, CoordType.DEST)
    );
  }

  removePickupCoords() {
    this.pickupCoords = [];
    this.coordsChange.next(new Coordinates(null, CoordType.PICKUP));
  }

  removeDestCoords() {
    this.destinationCoords = [];
    this.coordsChange.next(new Coordinates(null, CoordType.DEST));
  }

  setSelectedRoute(route: Route | null) {
    this.routeChange.next(route);
  }

  getAllActiveRides(): Observable<Ride[]> {
    return this.http.get<Ride[]>('http://localhost:8080/api/ride');
  }

  findRoutes(): Observable<any> { //routeJSON strings 
    return this.http.get<any>(`https://routing.openstreetmap.de/routed-car/route/v1/driving/${this.pickupCoords[1]},${this.pickupCoords[0]};${this.destinationCoords[1]},${this.destinationCoords[0]}?geometries=geojson&overview=false&alternatives=true&steps=true`)
  }

  enableDriver(): Observable<any> {
    return this.http.get<any>('http://localhost:8080/api/driver/activate/3');
  }
}
