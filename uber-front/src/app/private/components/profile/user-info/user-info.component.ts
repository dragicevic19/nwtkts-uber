import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/core/services/user/user.service';
import { User } from 'src/app/private/models/User';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss'],
})
export class UserInfoComponent implements OnInit {
  @Input() user!: User;
  @Output() changePassEvent = new EventEmitter<void>();

  edit: Boolean = false;
  editForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.editForm = this.fb.group({
      firstName: [
        this.user.firstName,
        [
          Validators.minLength(2),
          Validators.required,
          Validators.maxLength(32),
        ],
      ],
      lastName: [
        this.user.lastName,
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(32),
        ],
      ],
      street: [
        this.user.street,
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z0-9 ]*$/)],
      ],

      city: [
        this.user.city,
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z ]*$/)],
      ],
      country: [
        this.user.country,
        [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z ]*$/)],
      ],

      phone: [
        this.user.phone,
        [
          Validators.required,
          Validators.pattern(
            /^[0-9]*$/),
        ],
      ],
    });
  }

  onEditClick() {
    this.edit = true;
  }

  onChangePassClick() {
    this.changePassEvent.emit();
  }

  saveChangesClick() {
    this.edit = false;
    if (this.editForm.valid) {
      this.changeUserInfo(this.editForm.value);
      this.userService.editUserInfo(this.user).subscribe({
        next: (res) => {
          this.toastr.success('Successfully changed personal informations');
        },
        error: (err) => {
          this.toastr.error('An unexpected error occurred');
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.editForm);
    }
  }

  changeUserInfo(editedUser: User) {
    this.user.firstName = editedUser.firstName;
    this.user.lastName = editedUser.lastName;
    this.user.city = editedUser.city;
    this.user.country = editedUser.country;
    this.user.street = editedUser.street;
    this.user.phone = editedUser.phone;
  }
}
