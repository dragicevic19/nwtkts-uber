import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { VehicleType } from '../../models/VehicleType';
import { VehicleTypeSelection } from '../../models/VehicleTypeSelection';

@Component({
  selector: 'app-select-vehicle-list',
  templateUrl: './select-vehicle-list.component.html',
  styleUrls: ['./select-vehicle-list.component.scss']
})
export class SelectVehicleListComponent {

  @Input() vehicleTypeSelection!: VehicleTypeSelection;
  @Input() vehicleTypes!: VehicleType[];
  @Output() priceUpdateEvent = new EventEmitter<number>();

  typeSelected(id: number) {
    const prevVehicleType = this.vehicleTypes.find(item => item.id === this.vehicleTypeSelection.selectedTypeId)!;
    const newVehicleType = this.vehicleTypes.find(item => item.id === id)!
    this.priceUpdateEvent.emit(newVehicleType.additionalPrice - prevVehicleType.additionalPrice);
    this.vehicleTypeSelection.selectedTypeId = id;
  }
  onCheckedBabies() {
    this.vehicleTypeSelection.babies = !this.vehicleTypeSelection.babies;
  }
  onCheckedPets() {
    this.vehicleTypeSelection.pets = !this.vehicleTypeSelection.pets;
  }
}