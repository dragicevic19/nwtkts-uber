import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SortDirection } from '@angular/material/sort';
import { Observable } from 'rxjs';
import { RootObject } from '../models/ride-history/RootObject';

@Injectable({
  providedIn: 'root'
})
export class RideHistoryService {

  constructor(private httpClient: HttpClient) { }

  getRepoIssues(sort: string, order: SortDirection, page: number, pageSize: number): Observable<RootObject> {
    const href = 'http://localhost:8080/api/ride/history';
    // const requestUrl = `&sort=${sort}&order=${order}&page=${ page + 1 }`;

    const requestUrl = `${href}?page=${page}&size=${pageSize}&sort=${sort}&order=${order}`;

    return this.httpClient.get<RootObject>(requestUrl);
  }

}
