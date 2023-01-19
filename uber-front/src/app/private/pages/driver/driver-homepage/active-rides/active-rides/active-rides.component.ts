import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { User } from 'src/app/private/models/User';
import { DriversRide } from 'src/app/public/models/DriversRide';
import { Ride } from 'src/app/shared/models/Ride';

@Component({
  selector: 'app-active-rides',
  templateUrl: './active-rides.component.html',
  styleUrls: ['./active-rides.component.scss']
})
export class ActiveRidesComponent implements OnInit {

  activeRides: DriversRide[] = [];

  constructor(private driverService: DriverService, private toastr: ToastrService) { }



  ngOnInit(): void {
    this.driverService.getMyActiveRides().subscribe({
      next: (res) => {
        this.activeRides = res;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  onStartRide(ride: DriversRide) {
    ride.rideStatus = 'STARTED';
    this.driverService.startRide(ride.id).subscribe({
      next: (res) => {
        this.toastr.success('Ride started!');
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
