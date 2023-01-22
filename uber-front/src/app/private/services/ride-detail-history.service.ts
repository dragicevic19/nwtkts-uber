import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RideHistoryDetailsAdmin } from '../models/ride-history/RideHistoryDetailsAdmin';
import { RideHistoryDetailsClient } from '../models/ride-history/RideHistoryDetailsClient';
import { RideHistoryDetailsDriver } from '../models/ride-history/RideHistoryDetailsDriver';

@Injectable({
  providedIn: 'root'
})
export class RideDetailHistoryService {

  constructor(private httpClient: HttpClient) { }

  getRideDetailsClient(rideId: number): Observable<RideHistoryDetailsClient>{
    const href = 'http://localhost:8080/api/ride/details/client';
    // const requestUrl = `&sort=${sort}&order=${order}&page=${ page + 1 }`;

    const requestUrl = `${href}?rideId=${rideId}`;

    return this.httpClient.get<RideHistoryDetailsClient>(requestUrl);
  }

  getRideDetailsDriver(rideId: number): Observable<RideHistoryDetailsDriver> {
    const href = 'http://localhost:8080/api/ride/details/driver';

    const requestUrl = `${href}?rideId=${rideId}`;

    return this.httpClient.get<RideHistoryDetailsDriver>(requestUrl);
  }

  getRideDetailsAdmin(rideId: number): Observable<RideHistoryDetailsAdmin> {
    const href = 'http://localhost:8080/api/ride/details/admin';

    const requestUrl = `${href}?rideId=${rideId}`;

    return this.httpClient.get<RideHistoryDetailsAdmin>(requestUrl);
  }

}
