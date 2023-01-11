import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LatLngExpression } from 'leaflet';
import { Observable, Subject } from 'rxjs';
import { Coordinates } from '../models/Coordinates';
import { Ride } from '../models/Ride';
import { Route } from '../models/Route';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  pickupCoords: any;
  destinationCoords: any;
  additionalStops = new Map<number, Coordinates>();

  coordsChange: Subject<Coordinates> = new Subject<Coordinates>();
  routeChange: Subject<Route | null> = new Subject<Route | null>();

  constructor(private http: HttpClient) {
    this.pickupCoords = [];
    this.destinationCoords = [];
  }

  changePickup(coords: Array<number>) {
    this.pickupCoords = coords;
    this.coordsChange.next(new Coordinates(this.pickupCoords, 0));
  }

  changeDestination(coords: Array<number>) {
    this.destinationCoords = coords;
    this.coordsChange.next(new Coordinates(this.destinationCoords, 1));
  }

  changeAdditionalStop(index: number, coords: Coordinates | undefined) {
    if (!coords) throw new Error('Coordinates undefined');
    this.additionalStops.set(index, coords);
    coords.type += 2; // zato sto su pickup 0 i destination 1
    this.coordsChange.next(coords);
  }

  removePickupCoords() {
    this.pickupCoords = [];
    this.coordsChange.next(new Coordinates(null, 0));
  }

  removeDestCoords() {
    this.destinationCoords = [];
    this.coordsChange.next(new Coordinates(null, 1));
  }

  removeAdditionalStop(index: number) {
    this.additionalStops.delete(index);
    this.coordsChange.next(new Coordinates(null, index + 2));
  }

  setSelectedRoute(route: Route | null) {
    this.routeChange.next(route);
  }

  getAllActiveRides(): Observable<Ride[]> {
    return this.http.get<Ride[]>('http://localhost:8080/api/ride');
  }

  findRoutes(): Observable<any> {
    let coordsString = this.makeStringOfCoordinates();
    return this.http.get<any>(
      `https://routing.openstreetmap.de/routed-car/route/v1/driving/${coordsString}?geometries=geojson&overview=false&alternatives=true&steps=true`
    );

    // ovo sam probao sa drugim apijem zbog one najblize i najbrze rute.. i vise ruta za vise waypointa ali ne radi
    // const body = {
    //   // 'coordinates': [[19.848011,45.265405],[19.84268,45.246134],[19.825581,45.276002]],
    //   "coordinates": [[this.pickupCoords[1], this.pickupCoords[0]], [this.destinationCoords[1], this.destinationCoords[0]]],
    //   'alternative_routes': {'target_count':3},
    //   "preference":"recommended",
    // };
    
    // const headers = new HttpHeaders({
    //   'Authorization': '5b3ce3597851110001cf624806040a24c83e4008bab2a0c0e0523507',
    // });

    // const options = {'headers' : headers};

    // return this.http.post<any>(
    //   `https://api.openrouteservice.org/v2/directions/driving-car`, body, options
    // )
  }

  makeStringOfCoordinates(): string {
    let coordsStr: string =
      String(this.pickupCoords[1]) + ',' + String(this.pickupCoords[0]) + ';';

    coordsStr +=
      String(this.destinationCoords[1]) +
      ',' +
      String(this.destinationCoords[0]);

    this.additionalStops.forEach((value: Coordinates, key: number) => {
      if (!value.coords) throw new Error('Value is missing');
      let coordinates: string[] = value.coords.toString().split(',');
      coordsStr += ';' + coordinates[1] + ',' + coordinates[0];
    });

    return coordsStr;
  }

  enableDriver(): Observable<any> {
    return this.http.get<any>('http://localhost:8080/api/driver/activate/3');
  }
}
