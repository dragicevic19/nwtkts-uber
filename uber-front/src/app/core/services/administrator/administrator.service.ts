import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {
  private apiServerUrl = 'http://localhost:8080/api'

  constructor(private http: HttpClient) { }

  // getAllUsers() {
  //   return this.http.get('http://localhost:8080/api/getAllUsers');
  // }

  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiServerUrl}/getAllUsers`);
  }
}
