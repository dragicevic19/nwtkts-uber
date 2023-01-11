import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import ValidateForm from '../../helpers/validateform';
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
  counter = Array;

  rideForm!: FormGroup;
  pickup: boolean = true;
  routesJSON: Route[];
  selectedRouteIndex: number = 0;
  additionalAddressCount: number = 0;
  additionalAddresses = new Map<number, Coordinates>();
  additionalAddressesIds: number[] = [];

  title: string = PICKUP_TITLE;

  constructor(
    private fb: FormBuilder,
    private mapService: MapService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.routesJSON = [];
  }

  ngOnInit(): void {
    this.rideForm = this.fb.group({
      pickup: ['', [Validators.required]],
      destination: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.authService.isLoggedIn())
      throw new Error('Method not implemented.');
    else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  pickupSelected($event: any) {
    if (!$event) {
      this.rideForm.controls['pickup'].setValue('');
      this.title = PICKUP_TITLE;
      this.mapService.removePickupCoords();
      this.routesJSON = [];
      this.mapService.setSelectedRoute(null);
      return;
    }

    this.rideForm.controls['pickup'].setValue({
      lat: $event.properties.lat,
      lon: $event.properties.lon,
    });
    this.title = DESTINATION_TITLE;
    let address = $event.properties;

    if (this.mapService.pickupCoords.length !== 0) {
      this.mapService.removePickupCoords();
      this.routesJSON = [];
      this.mapService.setSelectedRoute(null);
    }
    this.mapService.changePickup([address.lat, address.lon]);

    if (this.mapService.destinationCoords) {
      this.findRoutes();
    } else {
      this.title = DESTINATION_TITLE;
    }
  }

  destinationSelected($event: any) {
    if (!$event) {
      this.rideForm.controls['destination'].setValue('');
      this.mapService.removeDestCoords();
      this.routesJSON = [];
      this.mapService.setSelectedRoute(null);
      return;
    }
    this.rideForm.controls['destination'].setValue({
      lat: $event.properties.lat,
      lon: $event.properties.lon,
    });
    let address = $event.properties;

    if (this.mapService.destinationCoords.length !== 0) {
      this.mapService.removeDestCoords();
      this.routesJSON = [];
      this.mapService.setSelectedRoute(null);
    }
    this.mapService.changeDestination([address.lat, address.lon]);

    if (this.mapService.pickupCoords) {
      this.findRoutes();
    } else {
      this.title = PICKUP_TITLE;
    }
  }

  findRoutes() {
    this.title = PICK_ROUTE_TITLE;
    if (this.rideForm.valid) {
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
    } else {
      ValidateForm.validateAllFormFields(this.rideForm);
    }
  }

  addMoreStops() {
    if (this.authService.isLoggedIn()) {
      if (this.rideForm.valid) {
        this.additionalAddressesIds.push(this.additionalAddressCount++);
      } else
        this.toastr.warning(
          'Please input pickup and destination locations first!'
        );
    } else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  additionalStopSelected($event: any, index: number) {
    this.routesJSON = [];
    this.mapService.setSelectedRoute(null);

    if (!$event) {
      this.additionalAddresses.delete(index);
      this.mapService.removeAdditionalStop(index);

      const myId = this.additionalAddressesIds.indexOf(index, 0);
      if (myId > -1) {                                            // brisem da ne bi ponovo izgenerisao isti id za input
        this.additionalAddressesIds.splice(myId, 1);
      }

      this.findRoutes();
      return;
    }

    if (this.mapService.additionalStops.get(index)) {
      this.mapService.removeAdditionalStop(index);
    }

    this.additionalAddresses.set(
      index,
      new Coordinates([$event.properties.lat, $event.properties.lon], index)
    );

    this.mapService.changeAdditionalStop(
      index,
      this.additionalAddresses.get(index)
    );

    this.findRoutes();
  }

  routeSelected(index: number) {
    this.selectedRouteIndex = index;
    this.mapService.setSelectedRoute(this.routesJSON[index]);
  }
}
