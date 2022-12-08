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
import DecodeJwt from 'src/app/helpers/decodeJwt';

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

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private auth: AuthService,
    private socialAuthService: SocialAuthService
  ) {}

  hideShowPass() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'fa-eye') : (this.eyeIcon = 'fa-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }

  goToSignUp() {
    // this.router.navigate(['signup'])
    // .then(()=>{
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/signup';
    // })
  }

  ngOnInit(): void {
    localStorage.removeItem('access_token');

    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });

    // @ts-ignore
    window.onGoogleLibraryLoad = () => {
      console.log("Google's One-tap sign in script loaded!");
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
      console.log(this.socialAuthService.authState);
      // localStorage.setItem('userObject', JSON.stringify(user));
      const signInInfo = new SocialSignInInfoDTO('jwt', undefined, user);
      this.sendSocial(signInInfo);
    });
  }

  // after google sign in
  handleCredentialResponse(response: CredentialResponse) {
    let decodedToken: any | null = null;
    try {
      this.auth.sendGoogleIdToken(response?.credential);
      decodedToken = JSON.parse(atob(response?.credential.split('.')[1]));
    } catch (e) {
      console.error('Error while trying to decode token', e);
    }
    const googleSignInInfo = new SocialSignInInfoDTO(
      response.credential,
      decodedToken
    );
    this.sendSocial(googleSignInInfo);
  }

  sendSocial(socialSignInInfo: SocialSignInInfoDTO) {
    this.auth.socialSignIn(socialSignInInfo).subscribe({
      next: (res) => {
        localStorage.setItem('access_token', res.accessToken);
        if (res.fullRegDone) this.router.navigate(['mainpage']);
        else this.router.navigate(['additionalInfo']);
      },
      error: (err) => {
        alert(err.message);
      },
    });
  }

  onLogin() {
    if (this.loginForm.valid) {
      const loginInfo = new LoginInfoDTO(this.loginForm.value as RawFormValue);
      this.auth.login(loginInfo).subscribe({
        next: (res) => {
          localStorage.setItem('access_token', res.accessToken);
          if (res.fullRegDone) this.router.navigate(['mainpage']);
          else this.router.navigate(['additionalInfo']);
        },
        error: (err) => {
          alert(err.message);
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.loginForm);
    }
  }

  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  sendGoogleIdToken(idToken: string) {}
}
