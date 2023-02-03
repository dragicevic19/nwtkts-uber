import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Coordinates } from '../models/Coordinates';
import { Ride } from '../models/Ride';
import { Route } from '../models/Route';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  
  stopLocations = new Map<number, Coordinates>();

  coordsChange: Subject<Coordinates> = new Subject<Coordinates>();
  routeChange: Subject<Route | null> = new Subject<Route | null>();

  constructor(private http: HttpClient) {
  }

  changeLocation(index: number, coords: Coordinates | undefined) {
    if (!coords) throw new Error('Coordinates undefined');
    this.stopLocations.set(index, coords);
    this.coordsChange.next(coords);
  }

  removeLocation(index: number) {
    this.stopLocations.delete(index);
    this.coordsChange.next(new Coordinates(null, index));
  }

  setSelectedRoute(route: Route | null) {
    this.routeChange.next(route);
  }

  getAllActiveRides(): Observable<Ride[]> {
    return this.http.get<Ride[]>('http://localhost:8080/api/ride');
  }

  findRoutes(): Observable<any> {
    const coordsString = this.makeStringOfCoordinates();
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
    let coordsStr = '';
    const ascMapKeys = new Map([...this.stopLocations.entries()].sort());

    ascMapKeys.forEach((value: Coordinates) => {
      if (!value.coords) throw new Error('Value is missing');
      const coordinates: string[] = value.coords.toString().split(',');
      coordsStr += + coordinates[1] + ',' + coordinates[0] + ';';
    });

    coordsStr = coordsStr.slice(0, -1);

    return coordsStr;
  }
}
