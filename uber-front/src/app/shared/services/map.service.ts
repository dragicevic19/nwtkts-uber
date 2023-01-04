import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ride } from '../models/Ride';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  constructor(private http: HttpClient) {}

  getAllActiveRides(): Observable<Ride[]> {
    return this.http.get<Ride[]>('api/ride');
  }
}
