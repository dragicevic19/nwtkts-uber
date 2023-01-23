import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RideHistoryDetailsDriver } from 'src/app/private/models/ride-history/RideHistoryDetailsDriver';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';

@Component({
  selector: 'app-ride-history-detailed-driver-modal',
  templateUrl: './ride-history-detailed-driver-modal.component.html',
  styleUrls: ['./ride-history-detailed-driver-modal.component.scss']
})
export class RideHistoryDetailedDriverModalComponent {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsDriver;

  // form!: FormGroup;

  constructor(private activeModal: NgbActiveModal, 
    private rideDeatilHistoryService: RideDetailHistoryService,
    private fb: FormBuilder,
    private toastr: ToastrService) {}


    ngOnInit() {
      this.rideDeatilHistoryService
      .getRideDetailsDriver(this.rideId).subscribe({
        next: (res) => {
          this.rideDetails = res;
        },
        error: (err) => {
          this.toastr.error('An unexpected error occurred');
        },
      });
  
    }

    closeModal() {
      this.activeModal.close('Modal Closed');
    }


}
