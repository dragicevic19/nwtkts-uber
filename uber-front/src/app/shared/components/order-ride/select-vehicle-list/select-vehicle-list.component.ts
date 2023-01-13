import { Component, Input } from '@angular/core';
import { RideRequest } from 'src/app/shared/models/RideRequest';
import { VehicleType } from 'src/app/shared/models/VehicleType';

@Component({
  selector: 'app-select-vehicle-list',
  templateUrl: './select-vehicle-list.component.html',
  styleUrls: ['./select-vehicle-list.component.scss']
})
export class SelectVehicleListComponent {

  @Input() rideRequest!: RideRequest
  @Input() vehicleTypes!: VehicleType[];

  typeSelected(id: number) {
    const newVehicleType = this.vehicleTypes.find(item => item.id === id)!;
    this.rideRequest.setNewVehicleType(newVehicleType);
  }
  onCheckedBabies() {
    this.rideRequest.babies = !this.rideRequest.babies;
  }
  onCheckedPets() {
    this.rideRequest.pets = !this.rideRequest.pets;
  }
}