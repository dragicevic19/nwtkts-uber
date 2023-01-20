import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { ChangePasswordDto } from 'src/app/private/models/ChangePasswordDto';
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
    hasPassword: false,
    driverActive: false,
  }

  constructor(private http: HttpClient) {}

  // whoAmI(): Observable<User> {
  //   return this.http.get<User>(`${this.baseUrl}whoami`);
  // }

  editUserInfo(editedUser: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}editInfo`, editedUser);
  }

  changeProfilePicture(downloadURL: string): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}changeProfilePic`, downloadURL);
  }

  changePassword(passwords: ChangePasswordDto) {
    return this.http.put<User>(`${this.baseUrl}changePassword`, passwords);
  }

  getLoggedInUser(): User {
    return this.loggedInUser;
  }
}
