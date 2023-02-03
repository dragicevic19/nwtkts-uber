import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RideService } from 'src/app/core/services/ride/ride.service';
import { VehicleService } from 'src/app/core/services/vehicle/vehicle.service';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { Ride } from 'src/app/shared/models/Ride';
import { RideRequest } from 'src/app/shared/models/RideRequest';
import { VehicleType } from 'src/app/shared/models/VehicleType';

@Component({
  selector: 'app-req-ride-from-fav-route',
  templateUrl: './req-ride-from-fav-route.component.html',
  styleUrls: ['./req-ride-from-fav-route.component.scss']
})
export class ReqRideFromFavRouteComponent implements OnInit {
  @Input() favRoute!: FavRoute;

  rideRequest: RideRequest = new RideRequest();
  schedule = false;
  vehicleTypes: VehicleType[] = [];


  constructor(private activeModal: NgbActiveModal, private toastr: ToastrService,
    private vehicleService: VehicleService, private rideService: RideService) { }

  ngOnInit(): void {
    if (!this.favRoute.selectedRoute) throw new Error("No selected route");
    this.favRoute.selectedRoute.legs = JSON.parse(this.favRoute.selectedRoute.legsStr);
    this.rideRequest.setNewRoute(this.favRoute.selectedRoute);
    this.rideRequest.addressValuesStr = this.favRoute.selectedRoute.addressValuesStr;

    this.vehicleService.getAllVehicleTypes().subscribe({
      next: (res: VehicleType[]) => {
        this.vehicleTypes = res;
        this.rideRequest.vehicleType = this.vehicleTypes[0];
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  closeModal() {
    this.activeModal.close();
  }

  onSubmit() {
    this.rideService.makeNewRideRequest(this.rideRequest).subscribe({
      next: (res: Ride) => {
        if (res.rideStatus === 'WAITING_FOR_PAYMENT') {
          this.toastr.info('Waiting for all clients in ride to pay...');
        }
        else if (res.rideStatus === 'WAITING_FOR_DRIVER_TO_FINISH') {
          this.toastr.success('Driver will come to you after he finishes his ride', 'Ride has been successfully ordered')
        }
        else if (res.rideStatus === 'SCHEDULED') {
          this.toastr.success('Ride is scheduled');
        }
        else if (res.rideStatus === 'TO_PICKUP') {
          this.toastr.success('Ride has been successfully ordered');
        }
        else if (res.rideStatus === 'CANCELED') {
          this.toastr.warning('Try with different vehicle options', 'Looks like there is no currently available drivers');
        }
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    })
  }

  onScheduleClick() {
    this.schedule = !this.schedule;
  }



}
