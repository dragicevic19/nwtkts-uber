import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SocialAuthService } from 'angularx-social-login';
import DecodeJwt from 'src/app/helpers/decodeJwt';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.scss'],
})
export class MainpageComponent {
  user = DecodeJwt.getUserFromAuthToken();

  constructor(
    private router: Router,
    public socialAuthService: SocialAuthService,
    private auth: AuthService
  ) {}

  logout(): void {
    this.auth.logout();
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }
}
