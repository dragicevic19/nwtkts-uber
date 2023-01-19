import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
// import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { Observable } from 'rxjs';
import { RideHistoryDetailsClient } from 'src/app/private/models/ride-history/RideHistoryDetailsClient';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsClient;

  form!: FormGroup;

  constructor(private activeModal: NgbActiveModal, 
    private rideDeatilHistoryService: RideDetailHistoryService,
    private fb: FormBuilder) {}

  ngOnInit() {
    this.rideDeatilHistoryService
    .getRideDetailsClient(this.rideId).subscribe({
      next: (res) => {
        this.rideDetails = res;
        console.log(this.rideDetails.price);
      },
      error: (err) => {
      },
    });

    this.form = this.fb.group({
      driverRating: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      vehicleRating: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onSend() {
    console.log(this.form.value.driverRating);
    console.log(this.form.value.vehicleRating);
    this.clearFields();
  }

  clearFields() {
    this.form.reset();
  }

}
