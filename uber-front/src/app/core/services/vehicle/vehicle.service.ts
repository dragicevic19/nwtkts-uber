import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';
import { VehicleType } from 'src/app/shared/models/VehicleType';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private baseUrl: string = 'http://localhost:8080/api/vehicle';

  constructor(private http: HttpClient) { }

  getAllVehicleTypes(): Observable<VehicleType[]> {
    return this.http.get<VehicleType[]>(`${this.baseUrl}/getAllTypes`);
  }
}
