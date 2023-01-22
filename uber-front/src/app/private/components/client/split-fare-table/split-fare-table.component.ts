import { Component, OnDestroy, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ClientService } from 'src/app/core/services/client/client.service';
import { WebsocketService } from 'src/app/core/services/websocket/websocket.service';
import { ClientsSplitFareRide } from 'src/app/private/models/ClientsSplitFareRide';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';

@Component({
  selector: 'app-split-fare-table',
  templateUrl: './split-fare-table.component.html',
  styleUrls: ['./split-fare-table.component.scss']
})
export class SplitFareTableComponent implements OnInit, OnDestroy {

  ridesToPay: ClientsSplitFareRide[] = [];
  loggedIn?: UserFromJwt;
  subscriptions: Subscription[] = [];

  constructor(private clientService: ClientService, private toastr: ToastrService, private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken();
    this.websocketService.initializeWebSocketConnection();

    this.clientService.getMySplitFareRequests().subscribe({
      next: (res) => {
        this.ridesToPay = res;
      },
      error: (err) => {
        console.log(err);
      }
    });

    this.subscriptions.push(this.websocketService.newSplitFareRequest.subscribe((ride: ClientsSplitFareRide) => {
      if (this.loggedIn && ride.clientIds.includes(this.loggedIn.id)) {
        this.ridesToPay.push(ride);
      }
    }));

  }

  ngOnDestroy(): void {
    for (let sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  onAccept(ride: ClientsSplitFareRide) {
    this.clientService.acceptSplitFareRequest(ride.id).subscribe({
      next: (res) => {
        this.ridesToPay = this.ridesToPay.filter(x => x.id !== ride.id);
        this.toastr.success('Successfully accepted ride!');
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    })
  }

  onCancel(ride: ClientsSplitFareRide) {
    this.clientService.cancelSplitFare(ride.id).subscribe({
      next: (res) => {
        this.ridesToPay = this.ridesToPay.filter(x => x.id !== ride.id);
        this.toastr.info('Ride is canceled');
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    })
  }
}
