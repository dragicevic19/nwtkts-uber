import { Component, ViewChild, AfterViewInit, Input } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { merge, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { RideHistoryDetailedUserModalComponent } from '../../components/rideHistory/ride-history-detailed-user-modal/ride-history-detailed-user-modal.component';
import { RootObjectGeoApify } from '../../models/geoapify/RootObjectGeoApify';
import { RideHistory } from '../../models/ride-history/RideHistory';
import { RideHistoryService } from '../../services/ride-history.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from '../../models/User';
import { RideHistoryDetailedDriverModalComponent } from '../../components/rideHistory/ride-history-detailed-driver-modal/ride-history-detailed-driver-modal.component';
import { RideHistoryDetailedAdminModalComponent } from '../../components/rideHistory/ride-history-detailed-admin-modal/ride-history-detailed-admin-modal.component';

@Component({
  selector: 'app-ride-history',
  templateUrl: './ride-history.component.html',
  styleUrls: ['./ride-history.component.scss'],
})
export class RideHistoryComponent implements AfterViewInit {
  displayedColumns: string[] = [
    'startTime',
    'calculatedDuration',
    'startLocation',
    'endLocation',
    'price',
  ];
  data: RideHistory[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  clickedRide = -1;

  user!: User;
  @Input() someone: User | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  homePath = '';

  constructor(
    private rideHistoryService: RideHistoryService,
    private modalService: NgbModal,
    private authService: AuthService
  ) { }

  setClickedRideId(i: number) {
    this.clickedRide = i;

    this.authService.whoAmI().subscribe({
      next: (res) => {
        this.user = res as User;
        if (this.user.role === 'ROLE_CLIENT') {
          this.homePath = '/uber';
          const modalRef = this.modalService.open(
            RideHistoryDetailedUserModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        } else if (this.user.role === 'ROLE_DRIVER') {
          this.homePath = '/uber/driver';
          const modalRef = this.modalService.open(
            RideHistoryDetailedDriverModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        } else if (this.user.role === 'ROLE_ADMIN') {
          this.homePath = '/admin/allUsers';
          const modalRef = this.modalService.open(
            RideHistoryDetailedAdminModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        }
      },
    });
  }

  getStreet(resultsOuter: RootObjectGeoApify) {
    return resultsOuter.results[0].street;
  }

  ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          if (this.someone === null) {
            return this.rideHistoryService.getHistorycalRides(
              this.sort.active,
              this.sort.direction,
              this.paginator.pageIndex,
              this.paginator.pageSize
            ).pipe(catchError(() => observableOf(null)));
          } else {
            return this.rideHistoryService.getHistorycalRidesForAdmin(
              this.someone?.id,
              this.sort.active,
              this.sort.direction,
              this.paginator.pageIndex,
              this.paginator.pageSize
            ).pipe(catchError(() => observableOf(null)));
          }
        }),
        map((data) => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isRateLimitReached = data === null;

          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          this.resultsLength = data.totalElements;
          return data.content;
        })
      )
      .subscribe((data) => {
        this.data = data;

      });
  }
}
