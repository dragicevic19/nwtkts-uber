import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { WebsocketService } from 'src/app/core/services/websocket/websocket.service';
import { DriversRide } from 'src/app/private/models/DriversRide';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import { RideCancelationModalComponent } from '../ride-cancelation-modal/ride-cancelation-modal.component';


@Component({
  selector: 'app-active-rides-table',
  templateUrl: './active-rides-table.component.html',
  styleUrls: ['./active-rides-table.component.scss']
})
export class ActiveRidesTableComponent implements OnInit, OnDestroy {


  activeRides: DriversRide[] = [];
  loggedIn?: UserFromJwt;
  subscriptions: Subscription[] = [];

  constructor(private driverService: DriverService, private toastr: ToastrService,
    private websocketService: WebsocketService, private modalService: NgbModal) {

  }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken()

    this.websocketService.initializeWebSocketConnection();
    this.driverService.getMyActiveRides().subscribe({
      next: (res) => {
        this.activeRides = res;
      },
      error: (err) => {
        console.log(err);
      }
    });

    this.subscriptions.push(this.websocketService.newRideForDriver.subscribe((ride: DriversRide) => {
      if (ride.driverId === this.loggedIn?.id) {
        if (!this.activeRides.find(x => x.id === ride.id))
          this.activeRides.push(ride);
      }
    }));

    this.subscriptions.push(this.websocketService.changeDriverRideStatus.subscribe((ride: DriversRide) => {
      if (ride.driverId === this.loggedIn?.id) {
        this.activeRides.map(x => (x.id === ride.id) ? x.rideStatus = ride.rideStatus : x.rideStatus = x.rideStatus); 4
      }
    }));

    this.subscriptions.push(this.websocketService.driverEndingRide.subscribe((ride: DriversRide) => {
      if (ride.driverId === this.loggedIn?.id) {
        this.activeRides = this.activeRides.filter(x => x.id !== ride.id);
      }
    }));
  }

  ngOnDestroy(): void {
    for (let sub of this.subscriptions) {
      sub.unsubscribe();
    }
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

  onCancelRide(ride: DriversRide) {
    const modalRef = this.modalService.open(
      RideCancelationModalComponent,
      { size: 'lg' }
    );
    modalRef.componentInstance.rideId = ride.id;
  }

  onFinishRide(ride: DriversRide) {
    this.driverService.endRide(ride.id).subscribe({
      next: (res) => {
        this.toastr.success('Ride ended!');
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
