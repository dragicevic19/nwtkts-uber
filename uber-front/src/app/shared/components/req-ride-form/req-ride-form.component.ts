import { Component } from '@angular/core';
import { MdbModalService, MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from 'src/app/private/models/User';
import { Coordinates } from '../../models/Coordinates';
import { Route } from '../../models/Route';
import { MapService } from '../../services/map.service';
import { ModalComponent } from '../add-friend-to-ride-modal/modal.component';

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

  addedFriends: User[] = [];
  pricePerPerson = {  // pravim objekat da bi poslao ovaj price u modal preko reference
    price: 0
  }

  title: string = PICKUP_TITLE;

  modalRef: MdbModalRef<ModalComponent> | null = null;

  constructor(
    private mapService: MapService,
    private authService: AuthService,
    private toastr: ToastrService,
    private modalService: MdbModalService
  ) {
    this.routesJSON = [];
  }

  openModal() {
    if (this.authService.isLoggedIn()) {
      let modalConfig = {
        data: {
          price: this.routesJSON[this.selectedRouteIndex].price,
          addedFriends: this.addedFriends,
          pricePerPerson: this.pricePerPerson
        }
      }
      this.modalRef = this.modalService.open(ModalComponent, modalConfig);
    }
    else {
      this.toastr.warning('Please login or sign up!');
    }
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
          this.routeSelected(0);
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
    this.pricePerPerson.price = Number((this.routesJSON[index].price / (this.addedFriends.length + 1)).toFixed(2));
  }
}