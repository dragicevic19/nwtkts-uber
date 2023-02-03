import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ClientService } from 'src/app/core/services/client/client.service';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { UserService } from 'src/app/core/services/user/user.service';
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

  constructor(private userService: UserService, private driverService: DriverService,
    private clientService: ClientService, private toastr: ToastrService,
    private websocketService: WebsocketService, private modalService: NgbModal) {

  }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken()

    this.websocketService.initializeWebSocketConnection();
    this.userService.getMyActiveRides().subscribe({
      next: (res) => {
        this.activeRides = res;
      },
      error: (err) => {
        console.log(err);
      }
    });

    this.subscriptions.push(this.websocketService.newRideForDriver.subscribe((ride: DriversRide) => {

      if (this.isClientsOrDriversRide(ride)) {
        if (!this.activeRides.find(x => x.id === ride.id))
          this.activeRides.push(ride);
      }
    }));

    this.subscriptions.push(this.websocketService.changeDriverRideStatus.subscribe((ride: DriversRide) => {

      if (this.isClientsOrDriversRide(ride)) {
        this.activeRides.map(x => {if(x.id === ride.id) x.rideStatus = ride.rideStatus;});
      }
    }));

    this.subscriptions.push(this.websocketService.driverEndingRide.subscribe((ride: DriversRide) => {

      if (this.isClientsOrDriversRide(ride)) {
        this.activeRides = this.activeRides.filter(x => x.id !== ride.id);
      }
    }));
  }

  ngOnDestroy(): void {
    for (const sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  isClientsOrDriversRide(ride: DriversRide) {
    return this.loggedIn && ((ride.clientIds.includes(this.loggedIn.id) || ride.driverId === this.loggedIn.id));
  }



  onStartRide(ride: DriversRide) {
    ride.rideStatus = 'STARTED';
    this.driverService.startRide(ride.id).subscribe({
      next: () => {
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
      next: () => {
        this.toastr.success('Ride ended!');
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  onReport(ride: DriversRide) {
    this.clientService.reportDriver(ride.id).subscribe({
      next: () => {
        this.toastr.success('Report for driver has been sent to administrator');
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    });
  }

}
