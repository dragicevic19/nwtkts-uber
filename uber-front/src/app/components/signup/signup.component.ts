import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SignInInfoDTO } from 'src/app/dto/signInInfo';
import ValidateForm, { passwordMatch } from 'src/app/helpers/validateform';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  type: string = 'password';
  isText: boolean = false;
  eyeIcon: string = 'fa-eye-slash';
  signupForm!: FormGroup;
  emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    localStorage.removeItem('access_token');

    this.signupForm = this.fb.group({
      firstName: [
        '',
        [
          Validators.minLength(2),
          Validators.required,
          Validators.maxLength(32),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(32),
        ],
      ],
      email: ['', [Validators.required, Validators.pattern(this.emailRegex)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(32),
        ],
      ],
      repPassword: ['', Validators.required],
    });

    this.signupForm.addValidators(
      passwordMatch(
        this.signupForm.get('password')!,
        this.signupForm.get('repPassword')!
      )
    );
  }

  hideShowPass() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'fa-eye') : (this.eyeIcon = 'fa-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }

  goToLogin() {
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }

  onSingup() {
    if (this.signupForm.valid) {
      this.auth.signUp(new SignInInfoDTO(this.signupForm.value)).subscribe({
        next: (res) => {
          this.toastr
            .success(
              'Please click on the link that has just been sent to your email account to verify your email.',
              'A verification link has been sent to your email account'
            )
            .onHidden.subscribe(() => {
              this.signupForm.reset();
              this.goToLogin();
            });
        },
        error: (err) => {
          if (err.status === 409)
            this.toastr.error(
              'An account is already registered with your email.',
              'Registration Error'
            );
          else
            this.toastr.error(
              'An unexpected error occurred',
              'Registration Error'
            );
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.signupForm);
    }
  }
}
