import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { timeout } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { ResetPasswordDto } from 'src/app/public/models/resetPasswordDto';
import ValidateForm, { passwordMatch } from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-reset-pass',
  templateUrl: './reset-pass.component.html',
  styleUrls: ['./reset-pass.component.scss'],
})
export class ResetPassComponent {
  type: string = 'password';
  isText: boolean = false;
  eyeIcon: string = 'fa-eye-slash';
  resetForm!: FormGroup;

  private token!: string;
  private email!: string;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.resetForm = this.fb.group({
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

    this.resetForm.addValidators(
      passwordMatch(
        this.resetForm.get('password')!,
        this.resetForm.get('repPassword')!
      )
    );

    this.token = this.route.snapshot.queryParams['token'];
    this.email = this.route.snapshot.queryParams['email'];
  }

  hideShowPass() {
    this.isText = !this.isText;
    this.isText ? (this.eyeIcon = 'fa-eye') : (this.eyeIcon = 'fa-eye-slash');
    this.isText ? (this.type = 'text') : (this.type = 'password');
  }

  onSubmit() {
    if (this.resetForm.valid) {
      const payload: ResetPasswordDto = {
        password: this.resetForm.controls['password'].value,
        repPassword: this.resetForm.controls['repPassword'].value,
        token: this.token,
        email: this.email,
      };
      this.auth.resetPassword(payload).subscribe({
        next: (res) => {
          this.toastr
            .success('Password successfully changed!')
            .onHidden.subscribe(
              () =>
                (window.location.href =
                  window.location.protocol +
                  '//' +
                  window.location.host +
                  '/login')
            );
        },
        error: (err) => {
          this.toastr.error('Error while trying to change the password');
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.resetForm);
    }
  }
}
