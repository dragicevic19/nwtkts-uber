import { Component, Input, OnInit } from '@angular/core';
import { latLng, tileLayer, geoJSON, LayerGroup } from 'leaflet';

@Component({
  selector: 'app-static-map',
  templateUrl: './static-map.component.html',
  styleUrls: ['./static-map.component.scss'],
})
export class StaticMapComponent implements OnInit {
  @Input() className!: string;
  @Input() routeJSON!: string;

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
  mainGroup: LayerGroup[] = [];

  ngOnInit(): void {
    this.showClientsRide(this.routeJSON);
  }

  showClientsRide(routeJSON: string) {
    const geoLayerRouteGroup: LayerGroup = new LayerGroup();
    for (const leg of JSON.parse(routeJSON)) {
      if (!leg) continue;
      for (const step of leg.steps) {
        const routeLayer = geoJSON(step.geometry);
        routeLayer.setStyle({ color: `green` });
        routeLayer.addTo(geoLayerRouteGroup);
      }
    }
    this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
  }
}
