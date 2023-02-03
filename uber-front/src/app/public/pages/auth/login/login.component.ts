import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import {
  FacebookLoginProvider,
  SocialAuthService,
} from 'angularx-social-login';
import { CredentialResponse } from 'google-one-tap';
import ValidateForm from 'src/app/shared/helpers/validateform';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { SocialSignInInfoDTO } from 'src/app/public/models/socialSignInInfo';
import DecodeJwt from 'src/app/shared/helpers/decodeJwt';
import { LoginResponse } from 'src/app/public/models/LoginResponse';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  type = 'password';
  isText = false;
  eyeIcon = 'fa-eye-slash';
  loginForm!: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private auth: AuthService,
    private socialAuthService: SocialAuthService,
    private toastr: ToastrService
  ) {}

  hideShowPass() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'fa-eye') : (this.eyeIcon = 'fa-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }

  ngOnInit(): void {
    localStorage.removeItem('access_token');

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });

    // @ts-ignore
    window.onGoogleLibraryLoad = () => {
      // @ts-ignore
      google.accounts.id.initialize({
        client_id:
          '580010731527-g2pjimi8f9u1q1apl9urmfsse1birc6m.apps.googleusercontent.com',
        callback: this.handleCredentialResponse.bind(this),
        auto_select: false,
        cancel_on_tap_outside: true,
      });
      // @ts-ignore
      google.accounts.id.renderButton(
        // @ts-ignore
        document.getElementById('googleButtonDiv'),
        {
          theme: 'outline',
          size: 'large',
          type: 'icon',
          text: 'sign_in_with',
          shape: 'circle',
        } // customization attributes
      );
      // @ts-ignore
      google.accounts.id.prompt(); // also display the One Tap dialog
    };

    // after facebook sign in
    this.socialAuthService.authState.subscribe((user) => {
      const signInInfo = new SocialSignInInfoDTO(user);
      this.sendSocial(signInInfo);
    });
  }

  // after google sign in
  handleCredentialResponse(response: CredentialResponse) {
    this.auth.sendGoogleIdToken(response?.credential).subscribe({
      next: (res) => {
        this.successLogin(res);
      },
      error: (err) => {
        this.loginError(err);
      },
    });
  }

  sendSocial(socialSignInInfo: SocialSignInInfoDTO) {
    this.auth.socialSignIn(socialSignInInfo).subscribe({
      next: (res) => {
        this.successLogin(res);
      },
      error: (err) => {
        this.loginError(err);
      },
    });
  }

  onLogin() {
    if (this.loginForm.valid) {
      this.auth.login(this.loginForm.value).subscribe({
        next: (res) => {
          this.successLogin(res);
        },
        error: (err) => {
          this.loginError(err);
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.loginForm);
    }
  }

  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  successLogin(res: LoginResponse) {
    localStorage.setItem('access_token', res.accessToken);
    const user = DecodeJwt.getUserFromAuthToken();
    if (user?.role === 'ROLE_CLIENT') {
      if (user?.fullRegDone) this.router.navigate(['/uber']);
      else
        window.location.href =
          window.location.protocol +
          '//' +
          window.location.host +
          '/additionalInfo';
    }
    else if (user?.role === 'ROLE_DRIVER') {
      this.router.navigate(['uber/driver']);
    }
    else if (user?.role === 'ROLE_ADMIN') {
      this.router.navigate(['/admin/allUsers']);
    }
  }

  loginError(err: HttpErrorResponse) {
    if (err.status === 409)
      this.toastr.error(
        'Please login with your email and password.\nOnly clients can sign-in with Facebook or Google',
        'Login Error'
      );
    else if (err.status === 401)
      this.toastr.error('Wrong email or password', 'Login Error');
    else if (err.status === 403)
      this.toastr.error('Please confirm your email address', 'Login Error');
    else this.toastr.error('An unexpected error occurred', 'Login Error');
  }
}
