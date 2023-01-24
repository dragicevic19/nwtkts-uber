import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';
import { BehaviorSubject } from 'rxjs';
import { VehicleType } from 'src/app/private/models/VehicleType';
import { Driver } from 'src/app/private/models/Driver';
import { Notification } from 'src/app/private/models/Notification';
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

  public getAllVehicleTypes(): Observable<VehicleType[]> {
    return this.http.get<VehicleType[]>(`${this.apiServerUrl}/getAllVehicleTypes`);
  }

  public createDriver(driver: Driver): Observable<Driver> {
    return this.http.post<Driver>(`${this.apiServerUrl}/createDriver`, driver)
  }

  public getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiServerUrl}/getNotifications`);
  }

  public chagneNotificationStatus(notification: Notification): Observable<Notification> {
    return this.http.put<Notification> (`${this.apiServerUrl}/changeNotificationStatus`, notification);
  }

}
