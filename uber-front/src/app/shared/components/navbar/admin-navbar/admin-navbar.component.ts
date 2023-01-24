import { Component, Input } from '@angular/core';
import { User } from 'src/app/private/models/User';
import { AuthService } from 'src/app/core/services/auth/auth.service';

@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.scss']
})
export class AdminNavbarComponent {

  @Input() user!: User;
  
  constructor(private auth: AuthService) {}

  logout() {
    this.auth.logout();
  }


}
