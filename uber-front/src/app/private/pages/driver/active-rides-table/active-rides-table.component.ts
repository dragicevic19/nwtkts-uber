import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { DriversRide } from 'src/app/public/models/DriversRide';

@Component({
  selector: 'app-active-rides-table',
  templateUrl: './active-rides-table.component.html',
  styleUrls: ['./active-rides-table.component.scss']
})
export class ActiveRidesTableComponent {

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
