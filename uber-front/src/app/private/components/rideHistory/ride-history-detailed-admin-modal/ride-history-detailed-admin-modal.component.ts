import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RideHistoryDetailsAdmin } from 'src/app/private/models/ride-history/RideHistoryDetailsAdmin';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';

@Component({
  selector: 'app-ride-history-detailed-admin-modal',
  templateUrl: './ride-history-detailed-admin-modal.component.html',
  styleUrls: ['./ride-history-detailed-admin-modal.component.scss']
})
export class RideHistoryDetailedAdminModalComponent implements OnInit{

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsAdmin;


  constructor(private activeModal: NgbActiveModal, 
    private rideDeatilHistoryService: RideDetailHistoryService,
    private toastr: ToastrService) {}

    ngOnInit() {
      this.rideDeatilHistoryService
      .getRideDetailsAdmin(this.rideId).subscribe({
        next: (res) => {
          this.rideDetails = res;
        },
        error: () => {
          this.toastr.error('An unexpected error occurred');
        },
      });
    }

    closeModal() {
      this.activeModal.close('Modal Closed');
    }

}
