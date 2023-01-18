import { Component } from '@angular/core';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent {

  rideId: number | null = null;

  constructor(public modalRef: MdbModalRef<RideHistoryDetailedUserModalComponent>) {}


}
