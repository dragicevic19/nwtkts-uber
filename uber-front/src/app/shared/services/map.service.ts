import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LatLngExpression } from 'leaflet';
import { Observable, Subject } from 'rxjs';
import { Coordinates, CoordType } from '../models/Coordinates';
import { Ride } from '../models/Ride';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  pickupCoords: any;
  destinationCoords: any;

  coordsChange: Subject<Coordinates> = new Subject<Coordinates>();

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

  getAllActiveRides(): Observable<Ride[]> {
    return this.http.get<Ride[]>('api/ride');
  }
}
