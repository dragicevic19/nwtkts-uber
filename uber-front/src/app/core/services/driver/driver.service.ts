import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  private privateBaseUrl: string = "http://localhost:8080/driver/"


  constructor(private http: HttpClient) { }

  startRide(rideId: number): Observable<void> {
    return this.http.post<void>(`${this.privateBaseUrl}startRide`, rideId);
  }

}
