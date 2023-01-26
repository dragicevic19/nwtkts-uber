import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import DecodeJwt from 'src/app/shared/helpers/decodeJwt';
import { AuthService } from '../../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  
  constructor(public auth: AuthService, public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: string = route.data['expectedRoles'];
    const user = DecodeJwt.getUserFromAuthToken();

    if (!user) {
      this.router.navigate(["/login"]);
      return false;
    }

    const roles: string[] = expectedRoles.split("|", 2);
    if (roles.indexOf(user.role) === -1) {
      if (user.role === 'ROLE_DRIVER') {
        this.router.navigate(['/uber/driver']);
      }
      else if (user.role === 'ROLE_CLIENT') {
        this.router.navigate(['/uber']);
      }
      else if (user.role === 'ROLE_ADMIN') {
        this.router.navigate(['/admin/allUsers']);
      }
      return false;
    }
    return true;
  }
}
