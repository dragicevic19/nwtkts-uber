import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoriteRouteServiceService {

  private privateBaseUrl: string = "http://localhost:8080/ride/"

  constructor(private http: HttpClient) { }

  newFavRoute(rideId: number): Observable<boolean>{
    return this.http.post<boolean>(`${this.privateBaseUrl}favRouteFromExistingRide/${rideId}`, rideId);
  }

}
