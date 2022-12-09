import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  AdditionalSignInFormData,
  AdditionalSignInInfoDTO,
} from 'src/app/dto/additionalSignInInfo';
import DecodeJwt, { UserFromJwt } from 'src/app/helpers/decodeJwt';
import ValidateForm from 'src/app/helpers/validateform';
import { AuthService } from 'src/app/services/auth.service';
import { ClientService } from 'src/app/services/client.service';

@Component({
  selector: 'app-additional-signup-info',
  templateUrl: './additional-signup-info.component.html',
  styleUrls: ['./additional-signup-info.component.scss'],
})
export class AdditionalSignupInfoComponent implements OnInit {
  addiSignupForm!: FormGroup;
  user?: UserFromJwt;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = DecodeJwt.getUserFromAuthToken();

    this.addiSignupForm = this.fb.group({
      street: [
        '',
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z0-9 ]*$/)],
      ],

      city: [
        '',
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z ]*$/)],
      ],
      country: [
        '',
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z ]*$/)],
      ],

      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(
            /^(\+\d{1,2}\s?)?1?\-?\.?\s?\(?\d{3}\)?[\s.-]?\d{3}[\s.-]?\d{4}$/
          ),
        ],
      ],
    });
  }

  onConfirm() {
    if (this.addiSignupForm.valid) {
      this.clientService
        .sendAdditionalSignUpInfo(
          new AdditionalSignInInfoDTO(this.addiSignupForm.value)
        )
        .subscribe({
          next: (res) => {
            this.router.navigate(['mainpage']);
            this.addiSignupForm.reset();
          },
          error: (err) => {
            console.log('errror');
            alert(err?.message);
          },
        });
    } else {
      ValidateForm.validateAllFormFields(this.addiSignupForm);
    }
  }
}
