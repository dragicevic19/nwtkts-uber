import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginInfoDTO } from '../dto/loginInfoDto';
import { SocialSignInInfoDTO } from '../dto/socialSignInInfo';
import { SignInInfoDTO } from '../dto/signInInfo';
import { AdditionalSignInInfoDTO } from '../dto/additionalSignInInfo';

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
}
