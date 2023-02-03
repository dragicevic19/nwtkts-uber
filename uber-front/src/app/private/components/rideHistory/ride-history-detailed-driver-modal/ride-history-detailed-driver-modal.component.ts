import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RideHistoryDetailsDriver } from 'src/app/private/models/ride-history/RideHistoryDetailsDriver';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';

@Component({
  selector: 'app-ride-history-detailed-driver-modal',
  templateUrl: './ride-history-detailed-driver-modal.component.html',
  styleUrls: ['./ride-history-detailed-driver-modal.component.scss']
})
export class RideHistoryDetailedDriverModalComponent implements OnInit {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsDriver;

  constructor(private activeModal: NgbActiveModal,
    private rideDeatilHistoryService: RideDetailHistoryService,
    private toastr: ToastrService) { }


  ngOnInit() {
    this.rideDeatilHistoryService
      .getRideDetailsDriver(this.rideId).subscribe({
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
