import { Component, Input } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-client-navbar',
  templateUrl: './client-navbar.component.html',
  styleUrls: ['./client-navbar.component.scss']
})
export class ClientNavbarComponent {


  @Input() user!: User;
  
  constructor(private auth: AuthService) {}

  logout() {
    this.auth.logout();
  }

  openRide() {
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/uber';
  }

}
