import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { VehicleType } from 'src/app/private/models/VehicleType';
import { Driver } from 'src/app/private/models/Driver';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-add-driver',
  templateUrl: './admin-add-driver.component.html',
  styleUrls: ['./admin-add-driver.component.scss'],
})



export class AdminAddDriverComponent {

  constructor(
    private toastr: ToastrService
  ) {}

  // @ts-ignore!
  @Input() administratorService: AdministratorService;
  years!: number[];
  vehicleTypes!: VehicleType[];
  // Polja iz forme:
  firstName!: string;
  lastName!: string;
  email!: string;
  phone!: string;
  password!: string;
  repeatPassword!: string;
  country!: string;
  city!: string;
  street!: string;
  make!: string;
  model!: string;
  selectedVehicleType!: number;
  selectedYear!: number;
  finalVehicleTypeId!: number;
  licensePlateNumber!: string;
  includeBabies = false;
  includePets = false;

  // Paterni:
  emailPattern = /^[^@]+@[^@]+\.[^@]+$/;
  licensePlatePattern=/^[A-Z]{2}-[0-9]{3}-[A-Z]{2}$/;
  phonePattern=/^[0-9]{8,11}$/;

  ngOnInit() {
    this.years = Array.from(
      { length: 70 },
      (_, i) => i + new Date().getFullYear() - 69
    );
    this.selectedYear = new Date().getFullYear();
    this.getAllVehicleTypes();
  }

  public onCheckboxChange() {
    console.log(`Include babies: ${this.includeBabies}`);
    console.log(`Include pets: ${this.includePets}`);
    // Perform any additional actions here, such as fetching data based on the checkbox selection
  }

  onVehicleTypeChanged(vehicleType: number) {
    this.finalVehicleTypeId = vehicleType;
    this.selectedVehicleType = vehicleType;
    console.log(this.selectedVehicleType);
  }

  onYearChange(year: number) {
    this.selectedYear = year;
    console.log(`Selected year: ${year}`);
  }

  public getAllVehicleTypes(): void {
    this.administratorService.getAllVehicleTypes().subscribe(
      (response: VehicleType[]) => {
        this.vehicleTypes = response;
        console.log(this.vehicleTypes);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public checkFields() {
    console.log(`First name: ${this.firstName}`);
    console.log(`Last name: ${this.lastName}`);
    console.log(`Email: ${this.email}`);
    console.log(`Phone: ${this.phone}`);
    console.log(`Password: ${this.password}`);
    console.log(`Repeated password: ${this.repeatPassword}`);
    console.log(`Country: ${this.country}`);
    console.log(`City: ${this.city}`);
    console.log(`Street: ${this.street}`);
    console.log(`Make: ${this.make}`);
    console.log(`Model: ${this.model}`);
    console.log(`Vehicle type: ${this.selectedVehicleType}`);
    console.log(`Make year: ${this.selectedYear}`);
    console.log(`License plate number: ${this.licensePlateNumber}`);
    console.log(`Babies allowed: ${this.includeBabies}`);
    console.log(`Pets allowed: ${this.includePets}`);

    // fali za sliku
    if(this.firstName == undefined || this.lastName == undefined || this.email == undefined || this.phone == undefined
    || this.password == undefined || this.repeatPassword == undefined || this.country == undefined || this.city == undefined ||
    this.street == undefined || this.make == undefined || this.model == undefined || this.selectedVehicleType == undefined || 
    this.selectedYear == undefined || this.licensePlateNumber == undefined) {
      this.toastr.error("All fields are required");
    } else {
      if (this.password != this.repeatPassword) {
        this.toastr.error("Password needs to be matched");
      }
      else if (this.password.length < 8 || this.repeatPassword.length < 8) {
        this.toastr.error("Password needs to be of minimum 8 characters");
      }
      else if (!this.phonePattern.test(this.phone)) {
        this.toastr.error("Phone must contain only 8-11 numbers!");
      }
      else if (!this.emailPattern.test(this.email)) {
        this.toastr.error("Enter valid email address.");
      }
      else if (!this.licensePlatePattern.test(this.licensePlateNumber)) {
        this.toastr.error("Enter valid license plate number.");
      }
      else {
        const driver: Driver = {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
          password: this.password,
          phone_number: this.phone,
          country: this.country,
          city: this.city,
          street: this.street,
          vehicleTypeId: this.finalVehicleTypeId,
          vehicleMake: this.make,
          vehicleModel: this.model,
          license_plate_number: this.licensePlateNumber,
          makeYear: this.selectedYear,
          petsAllowed: this.includePets,
          babiesAllowed: this.includeBabies
        }
        this.administratorService.createDriver(driver).subscribe(
          (response: Driver) => {
            console.log(response);
          },
          (error: HttpErrorResponse) => {
            alert(error.message);
          }
        )
        this.toastr.success("Driver was succesfuly created!")
        this.firstName = "",
        this.lastName = "",
        this.email = "",
        this.password = "",
        this.repeatPassword = "",
        this.phone = "",
        this.country = "",
        this.city = "",
        this.street = "",
        this.finalVehicleTypeId = 0,
        this.make = "",
        this.model = "",
        this.licensePlateNumber = "",
        this.selectedYear = 2023,
        this.includePets = false,
        this.includeBabies = false
      }
    }
  }
}
