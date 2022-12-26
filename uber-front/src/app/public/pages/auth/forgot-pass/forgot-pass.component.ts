import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-forgot-pass',
  templateUrl: './forgot-pass.component.html',
  styleUrls: ['./forgot-pass.component.scss'],
})
export class ForgotPassComponent implements OnInit {
  forgotForm!: FormGroup;

  constructor(
    private auth: AuthService,
    private fb: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.forgotForm.valid) {
      const email = this.forgotForm.controls['email'].value;
      this.forgotForm.reset();
      this.auth.forgotPassword(email).subscribe({
        next: (res) => {
          this.toastr.success(
            'Please click on the link that has just been sent to your email account to change your password.',
            'A link has been sent to your email account'
          );
        },
        error: (err) => {
          if (err.status === 400)
            this.toastr.error(
              'There is no registered account with this email.',
              'Wrong email'
            );
          else
            this.toastr.error(
              'An unexpected error occurred',
              'Password reset error'
            );
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.forgotForm);
    }
  }
}
