import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import * as SockJS from 'sockjs-client';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { DriversRide } from 'src/app/public/models/DriversRide';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import * as Stomp from 'stompjs';


@Component({
  selector: 'app-active-rides-table',
  templateUrl: './active-rides-table.component.html',
  styleUrls: ['./active-rides-table.component.scss']
})
export class ActiveRidesTableComponent {

  activeRides: DriversRide[] = [];
  private stompClient: any;
  loggedIn?: UserFromJwt;

  constructor(private driverService: DriverService, private toastr: ToastrService) {

  }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken()

    this.initializeWebSocketConnection();
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


  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }


  openGlobalSocket() {

    this.stompClient.subscribe('/map-updates/new-ride-for-driver',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        if (ride.driverId === this.loggedIn?.id) {

          if (!this.activeRides.find(x => x.id === ride.id))
            this.activeRides.push(ride);

        }
      }
    );

    this.stompClient.subscribe('/map-updates/change-drivers-ride-status',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        if (ride.driverId === this.loggedIn?.id) {
          this.activeRides.map(x => (x.id === ride.id) ? x.rideStatus = ride.rideStatus : x.rideStatus = x.rideStatus); 4
        }
      }
    );

    this.stompClient.subscribe('/map-updates/driver-ending-ride',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        if (ride.driverId === this.loggedIn?.id) {
          this.activeRides = this.activeRides.filter(x => x.id !== ride.id);
        }
      }
    );
  }

}
