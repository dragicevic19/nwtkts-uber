import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { VehicleType } from 'src/app/private/models/VehicleType';

@Component({
  selector: 'app-vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.scss']
})
export class VehicleDetailsComponent {

  constructor(
    private administratorService: AdministratorService
  ) {}

  @Input() make!: string;
  @Input() model!: string;
  @Input() selectedVehicleType!: number;
  @Input() selectedYear!: number;
  @Input() licensePlateNumber!: string;
  @Input() includeBabies = false;
  @Input() includePets = false;

  @Output() makeChange = new EventEmitter<string>();
  @Output() modelChange = new EventEmitter<string>();
  @Output() selectedVehicleTypeChange = new EventEmitter<number>();
  @Output() selectedYearChange = new EventEmitter<number>();
  @Output() licensePlateNumberChange = new EventEmitter<string>();
  @Output() includeBabiesChange = new EventEmitter<boolean>();
  @Output() includePetsChange = new EventEmitter<boolean>();

  finalVehicleTypeId!: number;

  ngOnInit() {
    this.years = Array.from(
      { length: 70 },
      (_, i) => i + new Date().getFullYear() - 69
    );
    this.selectedYear = new Date().getFullYear();
    this.getAllVehicleTypes();
  }

  years!: number[];
  vehicleTypes!: VehicleType[];

  public getAllVehicleTypes(): void {
    this.administratorService.getAllVehicleTypes().subscribe(
      (response: VehicleType[]) => {
        this.vehicleTypes = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  onChange(property: string) {
    if (property === 'make') {
      this.makeChange.emit(this.make);
    }
    else if (property === 'model') {
      this.modelChange.emit(this.model);
    }
    else if (property === 'selectedYear') {
      this.selectedYearChange.emit(this.selectedYear);
    }
    else if (property === 'selectedVehicleType') {
      this.selectedVehicleTypeChange.emit(this.selectedVehicleType);
    }
    else if (property === 'includeBabies') {
      this.includeBabiesChange.emit(this.includeBabies);
    }
    else if (property === 'includePets') {
      this.includePetsChange.emit(this.includePets);
    }
    else if (property === 'licensePlateNumber') {
      this.licensePlateNumberChange.emit(this.licensePlateNumber);
    }
    
  } 
}
