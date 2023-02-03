import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { LoginFormValue } from 'src/app/public/models/loginInfoDto';
import { LoginResponse } from 'src/app/public/models/LoginResponse';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthService],
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('login should return status 401 for bad credentials', () => {
    const loginInfo: LoginFormValue = { email: "mika@gmail.com", password: "fff" };

    service.login(loginInfo).subscribe({
      error: (err) => {
        expect(err.status).toEqual(401);
      }
    });

    const loginUrl = "http://localhost:8080/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 401,
    });
    httpMock.verify();
  });


  it('login should return jwt token', () => {
    const loginInfo: LoginFormValue = { email: "user@gmail.com", password: "123" };

    service.login(loginInfo).subscribe({
      next: (response: LoginResponse) => {
        expect(response.accessToken).toEqual("somejwttoken");
      },
    });
    const loginUrl = "http://localhost:8080/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush({
      accessToken: "somejwttoken",
      expiresIn: 20000
    });
    httpMock.verify();
  });


  it('signUp should return status code 200', () => {
    const signupInfo = {
      email: 'mail@gmail.com',
      firstName: "Pera",
      lastName: "Peric",
      password: "123",
      repPassword: "123",
    };

    service.signUp(signupInfo).subscribe({
      next: (response) => {
        expect(response.email).toBe('mail@gmail.com');
      }
    });
    const req = httpMock.expectOne(`http://localhost:8080/auth/signup`);
    expect(req.request.method).toBe('POST');
    req.flush({
      email: 'mail@gmail.com',
      firstName: 'Pera',
      lastName: 'Peric'
    });
    httpMock.verify();
  });

  it('signUp should return status code 409 when email already exists', () => {
    const signupInfo = {
      email: 'user@gmail.com',
      firstName: "Pera",
      lastName: "Peric",
      password: "123",
      repPassword: "123",
    };

    service.signUp(signupInfo).subscribe({
      error: (err) => {
        expect(err.status).toBe(409);
      }
    });
    const req = httpMock.expectOne(`http://localhost:8080/auth/signup`);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 409,
    });
    httpMock.verify();
  });
});
