import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdministratorService {
  private apiServerUrl = 'http://localhost:8080/api'

  constructor(private http: HttpClient) { }

  public users: User[] = [];

  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiServerUrl}/getAllUsers`);
  }

  private activeNavItem = new BehaviorSubject<string>('allUsers');
  activeNavItem$ = this.activeNavItem.asObservable();

  setActiveNavItem(navItem: string) {
    this.activeNavItem.next(navItem);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiServerUrl}/updateUser`, user);
  }

}