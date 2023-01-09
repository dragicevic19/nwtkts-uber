import { Component, OnInit } from '@angular/core';
import {
  latLng,
  tileLayer,
  marker,
  geoJSON,
  LayerGroup,
  icon,
} from 'leaflet';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Ride } from '../../models/Ride';
import { Vehicle } from '../../models/Vehicle';
import { MapService } from '../../services/map.service';
import { Coordinates, CoordType } from '../../models/Coordinates';
import { Route } from '../../models/Route';

const markerIcon = icon({
  iconUrl: 'assets/img/marker-icon.png',
  iconSize: [25, 41],
  iconAnchor: [10, 41],
  popupAnchor: [2, -40],
});

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

const carIcon = icon({
  iconUrl: 'assets/img/car.png',
  iconSize: [35, 45],
  iconAnchor: [18, 45],
});

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
})
export class MapComponent implements OnInit {
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
  private stompClient: any;
  selectedRoute: LayerGroup = new LayerGroup();

  constructor(private mapService: MapService) {
    this.mapService.coordsChange.subscribe((coordinates: Coordinates) => {
      if (!coordinates.coords) {
        this.mainGroup = this.mainGroup.filter(
          (lg: LayerGroup) => lg !== this.markers[coordinates.type]
        );
        delete this.markers[coordinates.type];
        return;
      }

      let geoLayerGroup: LayerGroup = new LayerGroup();
      let markerLayer = marker(coordinates.coords, {
        icon: (coordinates.type === CoordType.PICKUP) ? markerBlue : markerRed,
      });
      markerLayer.addTo(geoLayerGroup);

      this.markers[coordinates.type] = geoLayerGroup;
      this.mainGroup = [...this.mainGroup, geoLayerGroup];
    });
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.mapService.getAllActiveRides().subscribe((ret) => {
      for (let ride of ret) {
        let color = Math.floor(Math.random() * 16777215).toString(16);
        let geoLayerRouteGroup: LayerGroup = new LayerGroup();
        for (let step of JSON.parse(ride.routeJSON)['routes'][0]['legs'][0][
          'steps'
        ]) {
          let routeLayer = geoJSON(step.geometry);
          routeLayer.setStyle({ color: `#${color}` });
          routeLayer.addTo(geoLayerRouteGroup);
          this.rides[ride.id] = geoLayerRouteGroup;
        }
        let markerLayer = marker(
          [ride.vehicle.longitude, ride.vehicle.latitude],
          {
            icon: carIcon,
          }
        );
        markerLayer.addTo(geoLayerRouteGroup);
        this.vehicles[ride.vehicle.id] = markerLayer;
        this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
      }
    });


    // kada korisnik izabere rutu ona mu se prikazuje
    this.mapService.routeChange.subscribe((route: Route | null) => {
      this.mainGroup = this.mainGroup.filter(
        (lg: LayerGroup) => lg !== this.selectedRoute
      );

      if (!route) return;
      this.selectedRoute = new LayerGroup();

      for (let step of route.legs[0]['steps']) {
        let routeLayer = geoJSON(step.geometry);
        routeLayer.setStyle({ color: `#3397FF` });
        routeLayer.addTo(this.selectedRoute);
      }
      this.mainGroup = [...this.mainGroup, this.selectedRoute];
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
    this.stompClient.subscribe(
      '/map-updates/update-vehicle-position',
      (message: { body: string }) => {
        let vehicle: Vehicle = JSON.parse(message.body);
        let existingVehicle = this.vehicles[vehicle.id];
        existingVehicle.setLatLng([vehicle.longitude, vehicle.latitude]);
        existingVehicle.update();
      }
    );
    this.stompClient.subscribe(
      '/map-updates/new-ride',
      (message: { body: string }) => {
        let ride: Ride = JSON.parse(message.body);
        let geoLayerRouteGroup: LayerGroup = new LayerGroup();
        let color = Math.floor(Math.random() * 16777215).toString(16);
        for (let step of JSON.parse(ride.routeJSON)['routes'][0]['legs'][0][
          'steps'
        ]) {
          let routeLayer = geoJSON(step.geometry);
          routeLayer.setStyle({ color: `#${color}` });
          routeLayer.addTo(geoLayerRouteGroup);
          this.rides[ride.id] = geoLayerRouteGroup;
        }
        let markerLayer = marker(
          [ride.vehicle.longitude, ride.vehicle.latitude],
          {
            icon: carIcon,
          }
        );
        markerLayer.addTo(geoLayerRouteGroup);
        this.vehicles[ride.vehicle.id] = markerLayer;
        this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
      }
    );
    this.stompClient.subscribe(
      '/map-updates/ended-ride',
      (message: { body: string }) => {
        let ride: Ride = JSON.parse(message.body);
        this.mainGroup = this.mainGroup.filter(
          (lg: LayerGroup) => lg !== this.rides[ride.id]
        );
        delete this.vehicles[ride.vehicle.id];
        delete this.rides[ride.id];
      }
    );
    this.stompClient.subscribe(
      '/map-updates/delete-all-rides',
      (message: { body: string }) => {
        this.vehicles = {};
        this.rides = {};
        this.mainGroup = [];
      }
    );
  }
}