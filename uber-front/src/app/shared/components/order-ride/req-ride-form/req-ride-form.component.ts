import { Component } from '@angular/core';
import { MdbModalService, MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { RideService } from 'src/app/core/services/ride/ride.service';
import { VehicleService } from 'src/app/core/services/vehicle/vehicle.service';
import DecodeJwt from 'src/app/shared/helpers/decodeJwt';
import { Coordinates } from 'src/app/shared/models/Coordinates';
import { Ride } from 'src/app/shared/models/Ride';
import { RideRequest } from 'src/app/shared/models/RideRequest';
import { Route } from 'src/app/shared/models/Route';
import { VehicleType } from 'src/app/shared/models/VehicleType';
import { MapService } from 'src/app/shared/services/map.service';
import { ModalComponent } from '../add-friend-to-ride-modal/modal.component';

const EMPTY_STAR_SRC: string = "assets/img/empty_star.png";
const FULL_STAR_SRC: string = "assets/img/star.png";

@Component({
  selector: 'app-req-ride-form',
  templateUrl: './req-ride-form.component.html',
  styleUrls: ['./req-ride-form.component.scss'],
})
export class ReqRideFormComponent {

  rideRequest: RideRequest = new RideRequest();

  addressInputId: number = 2;
  addressCoordinates = new Map<number, Coordinates>();
  activeInputIds: number[] = [0, 1];
  pickupAndDestinationEntered: boolean = false;

  selectingRoutes: boolean = true;
  routesJSON: Route[];
  selectedRouteIndex: number = 0;

  vehicleTypes: VehicleType[] = [];

  modalRef: MdbModalRef<ModalComponent> | null = null;
  starSource: string = EMPTY_STAR_SRC;
  favRoute: boolean = false;
  schedule: boolean = false;

  constructor(
    private mapService: MapService,
    private authService: AuthService,
    private toastr: ToastrService,
    private modalService: MdbModalService,
    private vehicleService: VehicleService,
    private rideService: RideService,
    private driverService: DriverService,
  ) {
    this.routesJSON = [];
  }

  openModal() {
    if (this.authService.isLoggedIn()) {
      let modalConfig = {
        data: {
          rideRequest: this.rideRequest
        }
      }
      this.modalRef = this.modalService.open(ModalComponent, modalConfig);
    }
    else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  onScheduleClick() {
    const user = DecodeJwt.getUserFromAuthToken();
    if (user?.role == 'ROLE_DRIVER') {
      this.driverService.startRide(2).subscribe({
        next: (res) => {
          console.log(res);
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
    this.schedule = !this.schedule;
  }

  ngOnInit(): void {
    this.vehicleService.getAllVehicleTypes().subscribe({
      next: (res: VehicleType[]) => {
        this.vehicleTypes = res;
        this.rideRequest.vehicleType = this.vehicleTypes[0];
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  onSubmit() {
    if (this.authService.isLoggedIn()) {
      this.rideService.makeNewRideRequest(this.rideRequest).subscribe({
        next: (res: Ride) => {
          if (res.rideStatus === 'WAITING_FOR_PAYMENT') {
            if (res.driverId) {
              this.toastr.success('Driver will come to you after he finishes his ride', 'Ride has been successfully ordered')
            }
            else {
              this.toastr.info('Waiting for all clients in ride to pay...');
            }
          }
          else if (res.rideStatus === 'SCHEDULED') {
            this.toastr.success('Ride is scheduled');
          }
          else if (res.rideStatus === 'TO_PICKUP') {
            this.toastr.success('Ride has been successfully ordered');
          }
          else if (res.rideStatus === 'CANCELED') {
            this.toastr.warning('Try with different vehicle options', 'Looks like there is no currently available drivers');
          }
        },
        error: (err) => {
          this.toastr.error(err.error);
        }
      })
    }

    else {
      this.toastr.warning('Please login or sign up!');
    }
  }

  findRoutes() {
    this.pickupAndDestinationEntered = [...this.addressCoordinates.keys()].includes(0) && this.addressCoordinates.size > 1;

    if (this.pickupAndDestinationEntered) {
      this.mapService.findRoutes().subscribe({
        next: (res) => {
          for (let route of res.routes) {
            this.routesJSON.push(new Route(route, res));
          }
          this.routeSelected(0);
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  starSelected() {

    if (!this.authService.isLoggedIn()) {
      this.toastr.warning('Please login or sign up!');
      return;
    }

    if (!this.pickupAndDestinationEntered) {
      this.toastr.warning('Enter pickup and destination locations first!');
      return;
    }

    this.favRoute = !this.favRoute;
    if (this.favRoute) {
      this.addToFavoriteRoutes();
    }
    else {
      this.toastr.warning('You can remove favorite route from Favorite Routes page');
    }
  }

  addToFavoriteRoutes() {
    this.rideService.newFavRoute(this.rideRequest.selectedRoute!).subscribe({
      next: (res) => {
        this.toastr.success('Route added to favorites!')
      },
      error: (err) => {
        console.log(err);
        this.toastr.error('Some error occurred...');
      }
    });
    this.starSource = FULL_STAR_SRC;
  }


  addMoreStops() {
    if (this.authService.isLoggedIn()) {
      if (this.activeInputIds.length > this.addressCoordinates.size)
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

    this.favRoute = false;
    this.starSource = EMPTY_STAR_SRC;

    if (!$event) {                                            // klik na onaj x u input polju
      this.removeLocation(index);
      return;
    }

    if (this.mapService.stopLocations.get(index)) {
      this.mapService.removeLocation(index);
    }
    this.addressCoordinates.set(index, new Coordinates([$event.properties.lat, $event.properties.lon], index));
    this.rideRequest.addressValues.set(index, $event.properties.address_line1 + ', ' + $event.properties.address_line2);

    this.mapService.changeLocation(index, this.addressCoordinates.get(index));
    this.findRoutes();
  }

  private removeLocation(index: number) {
    this.mapService.removeLocation(index);
    this.addressCoordinates.delete(index);
    this.rideRequest.addressValues.delete(index);

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
    this.rideRequest.setNewRoute(this.routesJSON[index]);
  }

  selectVehicleClicked() {
    this.selectingRoutes = false;
  }
  selectRouteClicked() {
    this.selectingRoutes = true;
  }
}