import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RideCancelation } from 'src/app/private/models/RideCancelation';
import { User } from 'src/app/private/models/User';

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

  cancelRide(cancelationDTO: RideCancelation): Observable<void>  {
    return this.http.put<void>(`${this.privateBaseUrl}cancelRide`, cancelationDTO);
  }

  changeActive(active: boolean): Observable<User> {
    return this.http.put<User>(`${this.privateBaseUrl}changeActive`, active);
  }
}
