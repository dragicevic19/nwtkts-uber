import { HttpBackend, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RootObjectGeoApify } from '../models/geoapify/RootObjectGeoApify';

@Injectable({
  providedIn: 'root'
})
export class GeoApifyService {

  private httpClient: HttpClient;

  constructor(private httpBackend: HttpBackend) {
    this.httpClient = new HttpClient(httpBackend);
   }

  getAddress(lon: number, lat: number) : Observable<RootObjectGeoApify> {
    const href = 'https://api.geoapify.com/v1/geocode/reverse';
    const apiKey = 'ec1643040f2a4efda83f6a6c20c3d2d4';

    const requestUrl = `${href}?lat=${lat}&lon=${lon}&format=json&apiKey=${apiKey}`;

    // console.log(lat);
    // console.log(lon);

    return this.httpClient.get<RootObjectGeoApify>(requestUrl);
  }

}
