import { Component, Input } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-driver-navbar',
  templateUrl: './driver-navbar.component.html',
  styleUrls: ['./driver-navbar.component.scss']
})
export class DriverNavbarComponent {

  @Input() user!: User;
  
  constructor(private auth: AuthService) {}


  logout() {
    // TODO: ne moze da se logout dok ima voznje
    this.auth.logout();
  }

  onCheckedActive() {
    this.user!.driverActive = !this.user?.driverActive;
  }

}
