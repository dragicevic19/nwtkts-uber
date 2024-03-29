import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import {
  latLng,
  tileLayer,
  marker,
  geoJSON,
  LayerGroup,
  icon,
} from 'leaflet';


import { Ride } from '../../models/Ride';
import { Vehicle } from '../../models/Vehicle';
import { MapService } from '../../services/map.service';
import { Coordinates } from '../../models/Coordinates';
import { Route } from '../../models/Route';
import DecodeJwt, { UserFromJwt } from '../../helpers/decodeJwt';
import { ToastrService } from 'ngx-toastr';
import { ClientNotification } from '../../models/ClientNotification';
import { WebsocketService } from 'src/app/core/services/websocket/websocket.service';
import { Subscription } from 'rxjs';
import { ClientsSplitFareRide } from 'src/app/private/models/ClientsSplitFareRide';

const markerRed = icon({
  iconUrl: 'assets/img/marker-red.png',
  iconSize: [25, 41],
  iconAnchor: [10, 41],
  popupAnchor: [2, -40],
})

const markerBlue = icon({
  iconUrl: 'assets/img/marker-blue.png',
  iconSize: [25, 41],
  iconAnchor: [10, 41],
  popupAnchor: [2, -40],
})


const blueCarIcon = icon({
  iconUrl: 'assets/img/blue-car-marker.png',
  iconSize: [40, 40],
  iconAnchor: [18, 40],
})

const blackCarIcon = icon({
  iconUrl: 'assets/img/black-car-marker.png',
  iconSize: [35, 35],
  iconAnchor: [18, 35],
})

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
})
export class MapComponent implements OnInit, OnDestroy {
  @Input() className!: string;

  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '...',
      }),
    ],
    zoom: 14,
    center: latLng(45.253434, 19.831323),
  };
  vehicles: any = {};
  rides: any = {};
  markers: any = {};
  mainGroup: LayerGroup[] = [];
  selectedRoute: LayerGroup = new LayerGroup();
  loggedIn: UserFromJwt | undefined = undefined;
  subscriptions: Subscription[] = [];

  constructor(private mapService: MapService, private toastr: ToastrService, private websocketService: WebsocketService) { }

  ngOnInit(): void {
    this.websocketService.initializeWebSocketConnection();
    this.loggedIn = DecodeJwt.getUserFromAuthToken()
    this.subscriptions.push(this.mapService.getAllActiveRides().subscribe((ret) => {
      for (const ride of ret) {
        if (this.loggedIn && ride.clientIds.includes(this.loggedIn.id) || ride.driverId === this.loggedIn?.id && ride.rideStatus !== 'CRUISING') {
          this.showClientsRide(ride);
        }
        else {
          this.showJustCarMarker(ride);
        }
      }
    }));

    this.subscriptions.push(this.mapService.coordsChange.subscribe((coordinates: Coordinates) => {
      if (!coordinates.coords) {
        this.mainGroup = this.mainGroup.filter(
          (lg: LayerGroup) => lg !== this.markers[coordinates.type]
        );
        delete this.markers[coordinates.type];
        return;
      }
      const geoLayerGroup: LayerGroup = new LayerGroup();
      const markerLayer = marker(coordinates.coords, {
        icon: (coordinates.type === 0) ? markerBlue : markerRed,
      });
      markerLayer.addTo(geoLayerGroup);

      this.markers[coordinates.type] = geoLayerGroup;
      this.mainGroup = [...this.mainGroup, geoLayerGroup];
    }));

    // kada korisnik izabere rutu ona mu se prikazuje
    this.subscriptions.push(this.mapService.routeChange.subscribe((route: Route | null) => {
      this.mainGroup = this.mainGroup.filter(
        (lg: LayerGroup) => lg !== this.selectedRoute
      );

      if (!route) return;
      this.selectedRoute = new LayerGroup();

      for (const leg of route.legs) {
        for (const step of leg.steps) {
          const routeLayer = geoJSON(step.geometry);
          // routeLayer.setStyle({ color: `#3397FF` });
          routeLayer.addTo(this.selectedRoute);
        }
      }
      this.mainGroup = [...this.mainGroup, this.selectedRoute];
    }));


    this.subscriptions.push(this.websocketService.vehicleUpdatePosition.subscribe((vehicle: Vehicle) => {
      if (!this.vehicles[vehicle.id]) {
        return;
      }
      else {
        const existingVehicle = this.vehicles[vehicle.id];
        existingVehicle.setLatLng([vehicle.latitude, vehicle.longitude]);
        existingVehicle.update();
      }
    }));

    this.subscriptions.push(this.websocketService.newRide.subscribe((ride: Ride) => {
      if (this.isClientsOrDriversRide(ride)) {
        this.checkForNewRideNotifications(ride);
        this.showClientsRide(ride);
      }
      else {
        this.showJustCarMarker(ride);
      }
    }));

    this.subscriptions.push(this.websocketService.endRide.subscribe((ride: Ride) => {
      this.checkForEndRideNotifications(ride)

      if (ride.rideStatus === 'WAITING_FOR_CLIENT' || ride.rideStatus === 'ENDING') return;

      this.mainGroup = this.mainGroup.filter(
        (lg: LayerGroup) => lg !== this.rides[ride.id]
      );
      delete this.vehicles[ride.vehicle.id];
      delete this.rides[ride.id];
    }));


    this.subscriptions.push(this.websocketService.clientNotificationScheduled.subscribe((notification: ClientNotification) => {
      if (this.loggedIn && notification.clientIds.includes(this.loggedIn.id)) {
        this.toastr.info(notification.notification);
      }
    }));

    this.subscriptions.push(this.websocketService.splitFareChangeStatus.subscribe((ride: ClientsSplitFareRide) => {
      this.checkForSplitFareNotifications(ride);
    }));
  }


  ngOnDestroy() {
    for (const sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  showClientsRide(ride: Ride) {
    if (this.vehicles[ride.vehicle.id]) {
      // delete waiting driver
      this.mainGroup = this.mainGroup.filter(
        (lg: LayerGroup) => lg !== this.rides[ride.id]
      );
      delete this.vehicles[ride.vehicle.id];
      delete this.rides[ride.id];
    }

    const geoLayerRouteGroup: LayerGroup = new LayerGroup();
    for (const leg of JSON.parse(ride.routeJSON)) {
      for (const step of leg.steps) {
        const routeLayer = geoJSON(step.geometry);
        routeLayer.setStyle({ color: `green` });
        routeLayer.addTo(geoLayerRouteGroup);
      }
    }
    this.rides[ride.id] = geoLayerRouteGroup;

    const markerLayer = marker(
      [ride.vehicle.latitude, ride.vehicle.longitude],
      {
        icon: (ride.vehicle.available) ? blueCarIcon : blackCarIcon,
      }
    );
    markerLayer.addTo(geoLayerRouteGroup);
    this.vehicles[ride.vehicle.id] = markerLayer;
    this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
  }

  showJustCarMarker(ride: Ride) {
    if (this.vehicles[ride.vehicle.id]) {
      // delete waiting driver
      this.mainGroup = this.mainGroup.filter(
        (lg: LayerGroup) => lg !== this.rides[ride.id]
      );
      delete this.vehicles[ride.vehicle.id];
      delete this.rides[ride.id];
    }

    const geoLayerRouteGroup: LayerGroup = new LayerGroup();
    this.rides[ride.id] = geoLayerRouteGroup;

    const markerLayer = marker(
      [ride.vehicle.latitude, ride.vehicle.longitude],
      {
        icon: (ride.vehicle.available) ? blueCarIcon : blackCarIcon,
      }
    );
    markerLayer.addTo(geoLayerRouteGroup);
    this.vehicles[ride.vehicle.id] = markerLayer;
    this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
  }


  checkForNewRideNotifications(ride: Ride) {
    if (this.loggedIn?.role === 'ROLE_CLIENT') {
      if (ride.rideStatus === 'TO_PICKUP')
        this.toastr.info('Driver is coming to you!');
      else if (ride.rideStatus === 'STARTED')
        this.toastr.info('Your ride has started!');
    }
    else if (this.loggedIn?.role === 'ROLE_DRIVER') {
      if (ride.rideStatus === 'TO_PICKUP')
        this.toastr.info('New ride! Go to pickup location!');
    }

  }

  isClientsOrDriversRide(ride: Ride) {
    return this.loggedIn && ((ride.clientIds.includes(this.loggedIn.id) || ride.driverId === this.loggedIn.id) && ride.rideStatus !== 'CRUISING');
  }

  checkForEndRideNotifications(ride: Ride): void {
    if (this.loggedIn && (ride.clientIds.includes(this.loggedIn.id) || ride.driverId === this.loggedIn.id)) {
      if (this.loggedIn.role === 'ROLE_CLIENT') {
        if (ride.rideStatus === 'WAITING_FOR_CLIENT')
          this.toastr.info('Driver is waiting for you!');
        else if (ride.rideStatus === 'ENDED')
          this.toastr.info('Your ride has ended');
        else if (ride.rideStatus === 'CANCELED') {
          this.toastr.warning('Driver canceled ride!');
        }
      }
      else {
        if (ride.rideStatus === 'WAITING_FOR_CLIENT') {
          this.toastr.info('Wait for client and press Start button when ride begins');
        }
      }
    }
  }

  checkForSplitFareNotifications(ride: ClientsSplitFareRide) {
    if (this.loggedIn && (ride.clientIds.includes(this.loggedIn.id))) {


      if (ride.rideStatus === 'WAITING_FOR_DRIVER_TO_FINISH') {
        this.toastr.success('Driver will come to you after he finishes his ride', 'Split fare ride has been ordered')
      }
      else if (ride.rideStatus === 'SCHEDULED') {
        this.toastr.success('Split fare ride is scheduled');
      }
      else if (ride.rideStatus === 'TO_PICKUP') {
        this.toastr.success('All friends paid and system founded the driver', 'Split fare ride has been ordered');
      }
      else if (ride.rideStatus === 'CANCELED') {
        this.toastr.warning('Split fare ride has been canceled');
      }

    }
  }
}
