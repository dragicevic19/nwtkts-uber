import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { UserService } from 'src/app/core/services/user/user.service';
import { ChangePasswordDto } from 'src/app/private/models/ChangePasswordDto';
import { User } from 'src/app/private/models/User';
import ValidateForm, {
  passwordMatch,
} from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-change-pass',
  templateUrl: './change-pass.component.html',
  styleUrls: ['./change-pass.component.scss'],
})
export class ChangePassComponent implements OnInit {

  @Input() user!: User;
  @Output() changedPassEvent = new EventEmitter<void>();

  changePassForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.changePassForm = this.fb.group({
      currentPassword: ['', []],
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

    this.changePassForm.addValidators(
      passwordMatch(
        this.changePassForm.get('password')!,
        this.changePassForm.get('repPassword')!
      )
    );
  }

  onBack() {
    this.changedPassEvent.emit();
  }

  onSubmit() {
    if (this.changePassForm.valid) {
      this.userService.changePassword(this.changePassForm.value).subscribe({
        next: (res) => {
          this.changedPassEvent.emit();
          this.toastr
            .success('Please login again', 'Successfully changed password')
            .onHidden.subscribe(() => {
              this.authService.logout();
            });
        },
        error: (err) => {
          if (err.status == 400) {
            this.toastr.error('Current password is wrong!');
          } else {
            this.toastr.error('An unexpected error occurred');
          }
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.changePassForm);
    }
  }
}
