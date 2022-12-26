import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl: string = 'http://localhost:8080/user/';
  
  private loggedInUser: User = {
    id: 0,
    image: '',
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    country: '',
    city: '',
    street: '',
    role: '',
  }

  constructor(private http: HttpClient) {}

  whoAmI(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}whoami`);
  }

  getLoggedInUser(): User {
    return this.loggedInUser;
  }
}
