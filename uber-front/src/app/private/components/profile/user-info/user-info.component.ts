import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/core/services/user/user.service';
import { User } from 'src/app/private/models/User';
import { Notification } from 'src/app/private/models/Notification';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss'],
})
export class UserInfoComponent implements OnInit {
  @Input() user!: User;
  @Output() changePassEvent = new EventEmitter<void>();

  edit = false;
  editForm!: FormGroup;
  newInfo!: User;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.newInfo = Object.assign({}, this.user);
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
      const notification = this.createNotification();
      this.userService.createNotification(notification).subscribe({
        next: () => {
          this.toastr.info('Your request to edit profile has been sent to admin to approve')
        },
        error: () => {
          this.toastr.error('An unexpected error occurred');
        }
      })
    } else {
      ValidateForm.validateAllFormFields(this.editForm);
    }
  }

  changeUserInfo(editedUser: User) {
    this.newInfo.firstName = editedUser.firstName;
    this.newInfo.lastName = editedUser.lastName;
    this.newInfo.city = editedUser.city;
    this.newInfo.country = editedUser.country;
    this.newInfo.street = editedUser.street;
    this.newInfo.phone = editedUser.phone;
  }

  createNotification(){
    const not: Notification = {
      id: 0,
      oldInfo: this.user,
      newInfo: this.newInfo,
      status: 'pending'
    }
    return not;
  }
}
