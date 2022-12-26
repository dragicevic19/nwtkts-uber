import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import DecodeJwt from 'src/app/shared/helpers/decodeJwt';
import { AuthService } from '../../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  
  constructor(public auth: AuthService, public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    console.log("ROLE GUARD")
    const expectedRoles: string = route.data['expectedRoles'];

    console.log(expectedRoles);
    const user = DecodeJwt.getUserFromAuthToken();

    if (!user) {
      this.router.navigate(["/login"]);
      return false;
    }

    const roles: string[] = expectedRoles.split("|", 2);
    console.log(user.role);
    if (roles.indexOf(user.role) === -1) {
      console.log("usao u ovaj los if redirect");
      this.router.navigate(["/home"]);
      return false;
    }
    return true;
  }
}
