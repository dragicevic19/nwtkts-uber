import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginFormValue } from 'src/app/public/models/loginInfoDto';
import { ResetPasswordDto } from 'src/app/public/models/resetPasswordDto';
import { SignInInfoDTO } from 'src/app/public/models/signInInfo';
import { SocialSignInInfoDTO } from 'src/app/public/models/socialSignInInfo';
import { Observable } from 'rxjs';
import { User } from 'src/app/private/models/User';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private baseUrl: string = 'http://localhost:8080/auth/';

  constructor(private http: HttpClient) {}

  whoAmI(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}whoami`);
  }

  signUp(userObj: SignInInfoDTO) {
    return this.http.post<any>(`${this.baseUrl}signup`, userObj);
  }

  login(loginObj: LoginFormValue) {
    return this.http.post<any>(`${this.baseUrl}login`, loginObj);
  }

  socialSignIn(userInfo: SocialSignInInfoDTO) {
    return this.http.post<any>(
      `${this.baseUrl}oauth2/facebook/login`,
      userInfo
    );
  }

  sendGoogleIdToken(token: string) {
    return this.http.post<any>(`${this.baseUrl}oauth2/google/login`, token);
  }

  logout() {
    localStorage.removeItem('access_token');
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }

  forgotPassword(email: string) {
    return this.http.post<any>(`${this.baseUrl}forgotPassword`, email);
  }

  resetPassword(payload: ResetPasswordDto) {
    return this.http.post<any>(`${this.baseUrl}resetPassword`, payload);
  }

  isLoggedIn() {
    if (!localStorage.getItem('access_token')) {
      return false;
    }
    return true;
  }
}
