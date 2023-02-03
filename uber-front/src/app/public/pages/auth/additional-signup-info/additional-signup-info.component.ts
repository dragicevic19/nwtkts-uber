import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from 'src/app/core/services/client/client.service';
import { AdditionalSignInInfoDTO } from 'src/app/public/models/additionalSignInInfo';

import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import ValidateForm from 'src/app/shared/helpers/validateform';

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
    private router: Router,
    private toastr: ToastrService
  ) { }

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
            /^[0-9]*$/
          ),
        ],
      ],
    });
  }

  onConfirm() {
    if (this.addiSignupForm) {
      const data = new AdditionalSignInInfoDTO(this.addiSignupForm.value);
      this.addiSignupForm.reset();
      this.clientService
        .sendAdditionalSignUpInfo(data)
        .subscribe({
          next: () => {
            this.router.navigate(['uber']);
          },
          error: () => {
            this.toastr.error(
              'An unexpected error occurred',
              'Error'
            );
          },
        });
    } else {
      ValidateForm.validateAllFormFields(this.addiSignupForm);
    }
  }
}
