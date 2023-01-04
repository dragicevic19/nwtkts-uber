import { Component, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { map, marker } from 'leaflet';

@Component({
  selector: 'app-req-ride-form',
  templateUrl: './req-ride-form.component.html',
  styleUrls: ['./req-ride-form.component.scss'],
})
export class ReqRideFormComponent {

  rideForm!: FormGroup;
  pickup: boolean = true;
  _marker: any = null;

  constructor(
    private fb: FormBuilder  ) {}

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
      return;
    }

    this.rideForm.controls['pickup'].setValue({lat: $event.properties.lat, lon: $event.properties.lon})
    this.pickup = false;
    let address = $event.properties;

    // TODO: add marker to map

      if (this._marker) {
        this._marker.remove();
      }
      
      if (!address) {
        return;
      }
  
      // this._marker = marker([address.lat, address.lon]).addTo(this._map);
      // if (address.bbox && address.bbox.lat1 !== address.bbox.lat2 && address.bbox.lon1 !== address.bbox.lon2) {
      //   this._map.fitBounds([[address.bbox.lat1, address.bbox.lon1], [address.bbox.lat2, address.bbox.lon2]], { padding: [100, 100] })
      // } else {
      //   this._map.setView([address.lat, address.lon], 15);
      // }
  }

  destinationSelected($event: any) {
    if (!$event) {
      this.rideForm.controls['destination'].setValue('');
      return;
    }
    this.rideForm.controls['destination'].setValue({lat: $event.properties.lat, lon: $event.properties.lon})
    /* TODO:
      - dodaj marker
      - ako je i pickup selektovan onda udaljiti mapu da se prikaze ruta
    */
  }

  setLocation() {
    /* TODO:
      - proveriti da li se bira lokacija za pickup ili destination
      - omoguciti na mapi da moze da se selektuje adresa
    */
  }

}
