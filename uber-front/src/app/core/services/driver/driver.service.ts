import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DriversRide } from 'src/app/private/models/DriversRide';
import { RideCancelation } from 'src/app/private/models/RideCancelation';
import { Ride } from 'src/app/shared/models/Ride';
import { RideRequest } from 'src/app/shared/models/RideRequest';
import { RideService } from '../ride/ride.service';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  private privateBaseUrl: string = "http://localhost:8080/driver/"

  constructor(private http: HttpClient) { }

  startRide(rideId: number): Observable<void> {
    return this.http.post<void>(`${this.privateBaseUrl}startRide`, rideId);
  }

  endRide(rideId: number): Observable<void> {
    return this.http.put<void>(`${this.privateBaseUrl}endRide`, rideId);
  }

  cancelRide(cancelationDTO: RideCancelation)  {
    return this.http.put<any>(`${this.privateBaseUrl}cancelRide`, cancelationDTO);
  }

  getMyActiveRides(): Observable<DriversRide[]> {
    return this.http.get<DriversRide[]>(`${this.privateBaseUrl}myActiveRides`);
  }

}
