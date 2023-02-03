import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { LoginFormValue } from 'src/app/public/models/loginInfoDto';

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

    service.login(loginInfo).subscribe(data => {
      expect(data.status).toEqual(401);
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

    service.login(loginInfo).subscribe(data => {
      expect(data).toEqual("somejwttoken");
    });
    const loginUrl = "http://localhost:8080/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush("somejwttoken");
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

    service.signUp(signupInfo).subscribe(response => {
      expect(response.status).toEqual(201);
    });
    const req = httpMock.expectOne(`http://localhost:8080/auth/signup`);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 201,
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

    service.signUp(signupInfo).subscribe(response => {
      expect(response.status).toEqual(409);
    });
    const req = httpMock.expectOne(`http://localhost:8080/auth/signup`);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 409,
    });
    httpMock.verify();
  });
});
