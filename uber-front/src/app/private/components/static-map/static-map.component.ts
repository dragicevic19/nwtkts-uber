import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import {
  latLng,
  tileLayer,
  marker,
  geoJSON,
  LayerGroup,
  icon,
} from 'leaflet';

import { ToastrService } from 'ngx-toastr';
import { WebsocketService } from 'src/app/core/services/websocket/websocket.service';
import { Subscription } from 'rxjs';

import { Route } from '@angular/router';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import { ClientNotification } from 'src/app/shared/models/ClientNotification';
import { Coordinates } from 'src/app/shared/models/Coordinates';
import { Ride } from 'src/app/shared/models/Ride';
import { Vehicle } from 'src/app/shared/models/Vehicle';
import { MapService } from 'src/app/shared/services/map.service';

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
  selector: 'app-static-map',
  templateUrl: './static-map.component.html',
  styleUrls: ['./static-map.component.scss'],
})
export class StaticMapComponent implements OnInit {
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
    noWrap: true,
  };
  vehicles: any = {};
  rides: any = {};
  markers: any = {};
  mainGroup: LayerGroup[] = [];
  selectedRoute: LayerGroup = new LayerGroup();
  loggedIn: UserFromJwt | undefined = undefined;
  subscriptions: Subscription[] = [];

  constructor(private mapService: MapService, private toastr: ToastrService) {}

  ngOnInit(): void {
    
  };

}
  