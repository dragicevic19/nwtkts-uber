import { Component, OnInit } from '@angular/core';
import DecodeJwt, { UserFromJwt } from 'src/app/helpers/decodeJwt';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  user?: UserFromJwt;

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.user = DecodeJwt.getUserFromAuthToken();
  }

  loginClick() {
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }

  logout() {
    this.auth.logout();
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }
}
