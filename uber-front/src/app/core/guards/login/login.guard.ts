import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import DecodeJwt from 'src/app/shared/helpers/decodeJwt';
import { AuthService } from '../../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(public auth: AuthService, public router: Router) { }

  canActivate(): boolean {
    if (this.auth.isLoggedIn()) {
      const user = DecodeJwt.getUserFromAuthToken();
      if (user?.fullRegDone) this.router.navigate(['/uber']);
      else
        window.location.href =
          window.location.protocol +
          '//' +
          window.location.host +
          '/additionalInfo';
      return false;
    }
    return true;
  }
}
