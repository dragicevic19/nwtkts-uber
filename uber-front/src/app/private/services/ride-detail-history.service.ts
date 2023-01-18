import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RideHistoryDetailsClient } from '../models/ride-history/RideHistoryDetailsClient';

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

}
