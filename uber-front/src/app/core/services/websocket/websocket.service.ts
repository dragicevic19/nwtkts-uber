import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Subject } from 'rxjs';
import { Vehicle } from 'src/app/shared/models/Vehicle';
import { Ride } from 'src/app/shared/models/Ride';
import { ClientNotification } from 'src/app/shared/models/ClientNotification';
import { DriversRide } from 'src/app/private/models/DriversRide';
import { ClientsSplitFareRide } from 'src/app/private/models/ClientsSplitFareRide';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  private stompClient: any;
  vehicleUpdatePosition: Subject<Vehicle> = new Subject<Vehicle>();
  newRide: Subject<Ride> = new Subject<Ride>();
  endRide: Subject<Ride> = new Subject<Ride>();
  clientNotificationScheduled: Subject<ClientNotification> = new Subject<ClientNotification>();
  newRideForDriver: Subject<DriversRide> = new Subject<DriversRide>();
  changeDriverRideStatus: Subject<DriversRide> = new Subject<DriversRide>();
  driverEndingRide: Subject<DriversRide> = new Subject<DriversRide>();
  splitFareChangeStatus: Subject<ClientsSplitFareRide> = new Subject<ClientsSplitFareRide>();
  newSplitFareRequest: Subject<ClientsSplitFareRide> = new Subject<ClientsSplitFareRide>();

  constructor() { }


  initializeWebSocketConnection() {
    if (this.stompClient) return;
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();

    });
  }


  openGlobalSocket() {
    this.stompClient.subscribe('/map-updates/update-vehicle-position',
      (message: { body: string }) => {
        let vehicle: Vehicle = JSON.parse(message.body);
        console.log('SERVICE');
        
        this.vehicleUpdatePosition.next(vehicle);
      }
    );

    this.stompClient.subscribe('/map-updates/new-ride',
      (message: { body: string }) => {
        let ride: Ride = JSON.parse(message.body);
        this.newRide.next(ride);
      }
    );

    this.stompClient.subscribe('/map-updates/ended-ride',
      (message: { body: string }) => {
        let ride: Ride = JSON.parse(message.body);
        this.endRide.next(ride);

      }
    );

    // this.stompClient.subscribe('/map-updates/delete-all-rides',
    //   (message: { body: string }) => {
    //     this.vehicles = {};
    //     this.rides = {};
    //     this.mainGroup = [];
    //   }
    // );

    this.stompClient.subscribe('/map-updates/client-notifications-scheduled-ride-in',
      (message: { body: string }) => {
        let notification: ClientNotification = JSON.parse(message.body);
        this.clientNotificationScheduled.next(notification);
      }
    );


    this.stompClient.subscribe('/map-updates/new-ride-for-driver',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        this.newRideForDriver.next(ride);
      }
    );

    this.stompClient.subscribe('/map-updates/change-drivers-ride-status',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        this.changeDriverRideStatus.next(ride);
      }
    );

    this.stompClient.subscribe('/map-updates/driver-ending-ride',
      (message: { body: string }) => {
        let ride: DriversRide = JSON.parse(message.body);
        this.driverEndingRide.next(ride);
      }
    );
    
    this.stompClient.subscribe('/map-updates/split-fare-change-status',
    (message: {body: string}) => {
      let ride: ClientsSplitFareRide = JSON.parse(message.body);
      this.splitFareChangeStatus.next(ride);
    });

    this.stompClient.subscribe('/map-updates/new-split-fare-req',
    (message: {body: string}) => {
      let ride: ClientsSplitFareRide = JSON.parse(message.body);
      this.newSplitFareRequest.next(ride);
    });

  }
}
