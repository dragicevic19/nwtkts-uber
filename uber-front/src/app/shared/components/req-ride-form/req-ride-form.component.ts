import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { Coordinates } from '../../models/Coordinates';
import { Route } from '../../models/Route';
import { MapService } from '../../services/map.service';

const PICKUP_TITLE: string = 'Where can we pick you up?';
const DESTINATION_TITLE: string = 'Where to?';
const PICK_ROUTE_TITLE: string = 'Choose a ride';

@Component({
  selector: 'app-req-ride-form',
  templateUrl: './req-ride-form.component.html',
  styleUrls: ['./req-ride-form.component.scss'],
})
export class ReqRideFormComponent {

  routesJSON: Route[];
  selectedRouteIndex: number = 0;
  addressInputId: number = 2;
  addressValues = new Map<number, Coordinates>();
  activeInputIds: number[] = [0, 1];
  pickupAndDestinationEntered: boolean = false;

  title: string = PICKUP_TITLE;

  constructor(
    private mapService: MapService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.routesJSON = [];
  }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.authService.isLoggedIn())
      throw new Error('Method not implemented.');
    else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  findRoutes() {
    this.title = PICK_ROUTE_TITLE;
    this.pickupAndDestinationEntered = [...this.addressValues.keys()].includes(0) && this.addressValues.size > 1;

    if (this.pickupAndDestinationEntered) {
      this.mapService.findRoutes().subscribe({
        next: (res) => {
          for (let route of res.routes) {
            this.routesJSON.push(new Route(route));
          }
          this.selectedRouteIndex = 0;
          this.mapService.setSelectedRoute(this.routesJSON[0]);
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  addMoreStops() {
    if (this.authService.isLoggedIn()) {
      if (this.activeInputIds.length > this.addressValues.size)
        this.toastr.warning('You have available fields for destination');
      else 
        this.activeInputIds.push(this.addressInputId++);
    } else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  locationSelected($event: any, index: number) {
    this.routesJSON = [];
    this.mapService.setSelectedRoute(null);

    if (!$event) {                                            // klik na onaj x u input polju
      this.removeLocation(index);
      return;
    }

    if (this.mapService.stopLocations.get(index)) {
      this.mapService.removeLocation(index);
    }
    this.addressValues.set(index, new Coordinates([$event.properties.lat, $event.properties.lon], index));
    this.mapService.changeLocation(index, this.addressValues.get(index));
    this.findRoutes();
  }

  private removeLocation(index: number) {
    this.mapService.removeLocation(index);
    this.addressValues.delete(index);

    if (this.activeInputIds.length > 2 && index !== 0) {      // 2 inputa mi uvek ostaju
      const myId = this.activeInputIds.indexOf(index, 0);
      if (myId > -1) {                                        // brisem da ne bi ponovo izgenerisao isti id za input
        this.activeInputIds.splice(myId, 1);
      }
    }
    this.findRoutes();
  }

  routeSelected(index: number) {
    this.selectedRouteIndex = index;
    this.mapService.setSelectedRoute(this.routesJSON[index]);
  }
}