import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
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
  }
}
