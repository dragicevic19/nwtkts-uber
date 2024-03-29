import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SortDirection } from '@angular/material/sort';
import { Observable } from 'rxjs';
import { RideHistoryResponse } from '../models/ride-history/RideHistoryResponse';

@Injectable({
  providedIn: 'root'
})
export class RideHistoryService {

  constructor(private httpClient: HttpClient) { }

  getHistorycalRides(sort: string, order: SortDirection, page: number, pageSize: number): Observable<RideHistoryResponse> {
    const href = 'http://localhost:8080/api/ride/history';
    // const requestUrl = `&sort=${sort}&order=${order}&page=${ page + 1 }`;

    const requestUrl = `${href}?page=${page}&size=${pageSize}&sort=${sort}&order=${order}`;

    return this.httpClient.get<RideHistoryResponse>(requestUrl);
  }

  getHistorycalRidesForAdmin(id: number, sort: string, order: SortDirection, page: number, pageSize: number): Observable<RideHistoryResponse> {
    const href = 'http://localhost:8080/api/ride/history/forAdmin';
    // const requestUrl = `&sort=${sort}&order=${order}&page=${ page + 1 }`;

    const requestUrl = `${href}?id=${id}&page=${page}&size=${pageSize}&sort=${sort}&order=${order}`;

    return this.httpClient.get<RideHistoryResponse>(requestUrl);
  }

}
