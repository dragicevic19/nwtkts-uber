import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { Ride } from 'src/app/shared/models/Ride';
import { RideRequest } from 'src/app/shared/models/RideRequest';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private privateBaseUrl = "http://localhost:8080/ride/"

  constructor(private http: HttpClient) { }

  makeNewRideRequest(rideRequest: RideRequest): Observable<Ride> {
    return this.http.post<Ride>(`${this.privateBaseUrl}newRideRequest`, rideRequest.getRideRequestForRequest());
  }

  newFavRoute(selectedRoute: FavRoute): Observable<boolean>{
    return this.http.post<boolean>(`${this.privateBaseUrl}favRoute`, selectedRoute);
  }

  newFavRouteFromHistory(rideId: number): Observable<boolean> {
    return this.http.post<boolean>(`${this.privateBaseUrl}favRouteFromExistingRide/${rideId}`, rideId);
  }
}
