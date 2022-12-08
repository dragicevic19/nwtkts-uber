import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { SignInFormData, SignInInfoDTO } from 'src/app/dto/signInInfo';
import ValidateForm from 'src/app/helpers/validateform';
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

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    localStorage.removeItem('access_token');

    this.signupForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      repPassword: ['', Validators.required],
    });
  }

  hideShowPass() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'fa-eye') : (this.eyeIcon = 'fa-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }

  goToLogin() {
    // this.router.navigate(['login'])
    // .then(()=>{
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
    // })
  }

  onSingup() {
    if (this.signupForm.valid) {
      this.auth.signUp(new SignInInfoDTO(this.signupForm.value)).subscribe({
        next: (res) => {
          alert('Please check your email and verify');
          window.location.href =
            window.location.protocol + '//' + window.location.host + '/login';
          this.signupForm.reset();
        },
        error: (err) => {
          alert(err?.message);
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.signupForm);
    }
  }
}
