import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RideCancelation } from 'src/app/private/models/RideCancelation';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { Ride } from 'src/app/shared/models/Ride';
import { RideRequest } from 'src/app/shared/models/RideRequest';
import { Route } from 'src/app/shared/models/Route';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private privateBaseUrl: string = "http://localhost:8080/ride/"

  constructor(private http: HttpClient) { }

  makeNewRideRequest(rideRequest: RideRequest): Observable<Ride> {
    return this.http.post<Ride>(`${this.privateBaseUrl}newRideRequest`, rideRequest.getRideRequestForRequest());
  }

  newFavRoute(selectedRoute: FavRoute): Observable<boolean>{
    return this.http.post<boolean>(`${this.privateBaseUrl}favRoute`, selectedRoute);
  }

  putRideCancelation(cancelationDTO: RideCancelation)  {
    return this.http.put<any>(`${this.privateBaseUrl}driver/cancel`, cancelationDTO);
  }

}
