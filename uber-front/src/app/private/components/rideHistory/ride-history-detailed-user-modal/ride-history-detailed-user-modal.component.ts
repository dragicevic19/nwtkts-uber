import { Component } from '@angular/core';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { Observable } from 'rxjs';
import { RideHistoryDetailsClient } from 'src/app/private/models/ride-history/RideHistoryDetailsClient';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent {

  rideId!: number;

  rideDetails!: RideHistoryDetailsClient;

  constructor(public modalRef: MdbModalRef<RideHistoryDetailedUserModalComponent>,
    private rideDeatilHistoryService: RideDetailHistoryService) {}

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
  }

}
