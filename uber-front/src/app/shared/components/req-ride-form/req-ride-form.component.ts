import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  SocialAuthService,
  FacebookLoginProvider,
} from 'angularx-social-login';
import { CredentialResponse } from 'google-one-tap';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { LoginInfoDTO, RawFormValue } from 'src/app/public/models/loginInfoDto';
import { SocialSignInInfoDTO } from 'src/app/public/models/socialSignInInfo';
import ValidateForm from '../../helpers/validateform';

@Component({
  selector: 'app-req-ride-form',
  templateUrl: './req-ride-form.component.html',
  styleUrls: ['./req-ride-form.component.scss'],
})
export class ReqRideFormComponent {

loginWithFacebook() {
throw new Error('Method not implemented.');
}


  rideForm!: FormGroup;
  pickup: boolean = true;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private auth: AuthService,
    private socialAuthService: SocialAuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {

    this.rideForm = this.fb.group({
      pickup: ['', [Validators.required]],
      destination: ['', Validators.required],
    });
  }

  destinationInputFocus() {
    this.pickup = false;
  }

  pickupInputFocus() {
    this.pickup = true;
  }

  onSubmit() {
    throw new Error('Method not implemented.');
  }
}
