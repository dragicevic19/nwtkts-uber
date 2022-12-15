import { Component, NgZone, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import {
  FacebookLoginProvider,
  GoogleLoginProvider,
  SocialAuthService,
} from 'angularx-social-login';
import { CredentialResponse } from 'google-one-tap';
import { SocialSignInInfoDTO } from 'src/app/dto/socialSignInInfo';
import { LoginInfoDTO, RawFormValue } from 'src/app/dto/loginInfoDto';
import ValidateForm from 'src/app/helpers/validateform';
import { AuthService } from 'src/app/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  type: string = 'password';
  isText: boolean = false;
  eyeIcon: string = 'fa-eye-slash';
  loginForm!: FormGroup;
  emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

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
      email: ['', [Validators.required, Validators.pattern(this.emailRegex)]],
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
      const loginInfo = new LoginInfoDTO(this.loginForm.value as RawFormValue);
      this.auth.login(loginInfo).subscribe({
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

  successLogin(res: any) {
    localStorage.setItem('access_token', res.accessToken);
    if (res.fullRegDone) this.router.navigate(['mainpage']);
    else
      window.location.href =
        window.location.protocol +
        '//' +
        window.location.host +
        '/additionalInfo';
  }

  loginError(err: any) {
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
