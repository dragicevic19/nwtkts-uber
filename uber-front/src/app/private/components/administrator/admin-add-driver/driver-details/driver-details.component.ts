import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-driver-details',
  templateUrl: './driver-details.component.html',
  styleUrls: ['./driver-details.component.scss']
})
export class DriverDetailsComponent {

  @Input() firstName!: string;
  @Input() lastName!: string;
  @Input() email!: string;
  @Input() phone!: string;
  @Input() password!: string;
  @Input() repeatPassword!: string;
  @Input() country!: string;
  @Input() city!: string;
  @Input() street!: string;

  @Output() firstNameChange = new EventEmitter<string>();
  @Output() lastNameChange = new EventEmitter<string>();
  @Output() emailChange = new EventEmitter<string>();
  @Output() phoneChange = new EventEmitter<string>();
  @Output() passwordChange = new EventEmitter<string>();
  @Output() repeatPasswordChange = new EventEmitter<string>();
  @Output() countryChange = new EventEmitter<string>();
  @Output() cityChange = new EventEmitter<string>();
  @Output() streetChange = new EventEmitter<string>();

  onChange(property: string) {
    if (property === 'firstName') {
      this.firstNameChange.emit(this.firstName);
    }
    else if (property === 'lastName') {
      this.lastNameChange.emit(this.lastName);
    }
    else if (property === 'email') {
      this.emailChange.emit(this.email);
    }
    else if (property === 'phone') {
      this.phoneChange.emit(this.phone);
    }
    else if (property === 'password') {
      this.passwordChange.emit(this.password);
    }
    else if (property === 'repeatPassword') {
      this.repeatPasswordChange.emit(this.repeatPassword);
    }
    else if (property === 'country') {
      this.countryChange.emit(this.country);
    }
    else if (property === 'city') {
      this.cityChange.emit(this.city);
    }
    else if (property === 'street') {
      this.streetChange.emit(this.street);
    }
  }

}
