import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { latLng, tileLayer, marker, geoJSON, LayerGroup, icon } from 'leaflet';

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

  @Input()
  routeJSON!: string;

  constructor(private mapService: MapService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.showClientsRide(this.routeJSON);
  }

  showClientsRide(routeJSON: string) {
    let geoLayerRouteGroup: LayerGroup = new LayerGroup();
    for (let leg of JSON.parse(routeJSON)) {
      for (let step of leg?.steps) {
        let routeLayer = geoJSON(step.geometry);
        routeLayer.setStyle({ color: `green` });
        routeLayer.addTo(geoLayerRouteGroup);
      }
    }
    this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
  }
}
