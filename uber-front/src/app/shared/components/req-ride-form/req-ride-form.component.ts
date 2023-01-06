import { Component, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MapService } from '../../services/map.service';

@Component({
  selector: 'app-req-ride-form',
  templateUrl: './req-ride-form.component.html',
  styleUrls: ['./req-ride-form.component.scss'],
})
export class ReqRideFormComponent {

  rideForm!: FormGroup;
  pickup: boolean = true;

  constructor(
    private fb: FormBuilder,
    private mapService: MapService ) {}

  ngOnInit(): void {
    this.rideForm = this.fb.group({
      pickup: ['', [Validators.required]],
      destination: ['', Validators.required],
    });
  }

  onSubmit() {
    throw new Error('Method not implemented.');
  } 

  pickupSelected($event: any) {
    if (!$event) {
      this.rideForm.controls['pickup'].setValue('');
      this.pickup = true;
      this.mapService.removePickupCoords();
      return;
    }

    this.rideForm.controls['pickup'].setValue({lat: $event.properties.lat, lon: $event.properties.lon})
    this.pickup = false;
    let address = $event.properties;

    if (this.mapService.pickupCoords.length !== 0) {
      this.mapService.removePickupCoords();
    }
    this.mapService.changePickup([address.lat, address.lon]);
  }

  destinationSelected($event: any) {
    if (!$event) {
      this.rideForm.controls['destination'].setValue('');
      this.mapService.removeDestCoords();
      return;
    }
    this.rideForm.controls['destination'].setValue({lat: $event.properties.lat, lon: $event.properties.lon})
    let address = $event.properties;

    if (this.mapService.destinationCoords.length !== 0) {
      this.mapService.removeDestCoords();
    }
    this.mapService.changeDestination([address.lat, address.lon]);

    if (this.mapService.pickupCoords) {
      this.findRoutes();
      // izabrane su i pickup koordinate - nadji rute
    }
    else {
      this.pickup = true;
    }
  }


  findRoutes() {
    console.log('trazim rute')
  }

  setLocation() {
    /* TODO:
      - proveriti da li se bira lokacija za pickup ili destination
      - omoguciti na mapi da moze da se selektuje adresa
    */
  }

}
