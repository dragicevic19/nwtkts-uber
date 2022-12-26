import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginInfoDTO } from 'src/app/public/models/loginInfoDto';
import { ResetPasswordDto } from 'src/app/public/models/resetPasswordDto';
import { SignInInfoDTO } from 'src/app/public/models/signInInfo';
import { SocialSignInInfoDTO } from 'src/app/public/models/socialSignInInfo';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
 
  private baseUrl: string = 'http://localhost:8080/auth/';

  constructor(private http: HttpClient) {}

  signUp(userObj: SignInInfoDTO) {
    return this.http.post<any>(`${this.baseUrl}signup`, userObj);
  }

  login(loginObj: LoginInfoDTO) {
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
  }

  forgotPassword(email: string) {
    return this.http.post<any>(`${this.baseUrl}forgotPassword`, email);
  }

  resetPassword(payload: ResetPasswordDto) {
    return this.http.post<any>(`${this.baseUrl}resetPassword`, payload);
  }

  isLoggedIn() {
    if (!localStorage.getItem("access_token")) {
      return false;
    }
    return true;
  }
}
