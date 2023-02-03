import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth/auth.service';

import { of } from 'rxjs';
import { Router } from '@angular/router';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService, SocialAuthServiceConfig } from 'angularx-social-login';
import { ToastrService } from 'ngx-toastr';
import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let authenticationService: AuthService;
  let router: Router;

  beforeEach(async () => {
    const authServiceSpy = {
      login: jasmine.createSpy('login').and.returnValue(of({
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcHJpbmctc2VjdXJpdHktZXhhbXBsZSIsInN1YiI6InVzZXJAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfQ0xJRU5UIiwiaWQiOjQsImZpcnN0TmFtZSI6IktvcmlzbmlrIiwibGFzdE5hbWUiOiJLb3Jpc25pYyIsImZ1bGxSZWdEb25lIjp0cnVlLCJhdWQiOiJ3ZWIiLCJpYXQiOjE2NzUzNDU4NTEsImV4cCI6MTY3NTM3NDY1MX0.yCVJ23HwSdAougEYOrWW9NsqynKEIZ0Bw4oxEWZ6_dtu4zxM6Kl6ulHHXuT7kFQk5ZGa-Vu2A77d072xK-xqcQ",
        "expiresIn": 28800000,
      }))
    }

    const routerSpy = jasmine.createSpyObj('router', ['navigate']);
    const toastrSpy = jasmine.createSpyObj('toastr', ['success']);

    await TestBed.configureTestingModule({
      declarations: [LoginFormComponent],
      imports: [
        FormsModule,
        ReactiveFormsModule,
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ToastrService, useValue: toastrSpy },
        SocialAuthService,
        {
          provide: 'SocialAuthServiceConfig',
          useValue: {
            autoLogin: false,
            providers: [
              {
                id: GoogleLoginProvider.PROVIDER_ID,
                provider: new GoogleLoginProvider(
                  '580010731527-g2pjimi8f9u1q1apl9urmfsse1birc6m.apps.googleusercontent.com'
                ),
              },
              {
                id: FacebookLoginProvider.PROVIDER_ID,
                provider: new FacebookLoginProvider('876191489714176'),
              },
            ],
          } as SocialAuthServiceConfig,
        },
        FormBuilder,
      ]
    })
      .compileComponents();
    // fixture = TestBed.createComponent(LoginFormComponent);
    // component = fixture.componentInstance;
    // fixture.detectChanges();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authenticationService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should be initialized', () => {
    component.ngOnInit();
    expect(component.loginForm).toBeDefined();
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when email and password are empty', () => {
    component.loginForm.controls['email'].setValue('');
    component.loginForm.controls['password'].setValue('');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when email is empty', () => {
    component.loginForm.controls['email'].setValue('');
    component.loginForm.controls['password'].setValue('123');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when password is empty', () => {
    component.loginForm.controls['email'].setValue('user@gmail.com');
    component.loginForm.controls['password'].setValue('');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when email is invalid', () => {
    component.loginForm.controls['email'].setValue('user.com');
    component.loginForm.controls['password'].setValue('123');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should login on submit and redirect client to his homepage', () => {
    component.loginForm.controls['email'].setValue('user@gmail.com');
    component.loginForm.controls['password'].setValue('123');

    expect(component.loginForm.invalid).toBeFalsy();

    component.onLogin();
    expect(authenticationService.login).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/uber']);
  });
});
