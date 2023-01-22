import { HttpBackend, HttpClient } from '@angular/common/http';
import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, SortDirection } from '@angular/material/sort';
// import { MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import { lastValueFrom, merge, Observable, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { RideHistoryDetailedUserModalComponent } from '../../components/rideHistory/ride-history-detailed-user-modal/ride-history-detailed-user-modal.component';
import { RootObjectGeoApify } from '../../models/geoapify/RootObjectGeoApify';
import { RideHistory } from '../../models/ride-history/RideHistory';
import { RideHistoryResponse } from '../../models/ride-history/RideHistoryResponse';
import { GeoApifyService } from '../../services/geo-apify.service';
import { RideHistoryService } from '../../services/ride-history.service';

import { ViewEncapsulation } from '@angular/core';
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
  // rideHistoryService!: RideHistoryService | null;
  // geoApifyService!: GeoApifyService | null;
  data: RideHistory[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  clickedRide = -1;

  user!: User;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private rideHistoryService: RideHistoryService,
    private geoApifyService: GeoApifyService,
    private modalService: NgbModal,
    private authService: AuthService
  ) {}

  setClickedRideId(i: number) {
    this.clickedRide = i;
    console.log(this.clickedRide);

    const token = localStorage.getItem('access_token');
    console.log(token);

    this.authService.whoAmI().subscribe({
      next: (res) => {
        this.user = res as User;
        if (this.user.role === 'ROLE_CLIENT') {
          const modalRef = this.modalService.open(
            RideHistoryDetailedUserModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        } else if (this.user.role === 'ROLE_DRIVER') {
          const modalRef = this.modalService.open(
            RideHistoryDetailedDriverModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        } else if (this.user.role === 'ROLE_ADMIN') {
          const modalRef = this.modalService.open(
            RideHistoryDetailedAdminModalComponent,
            { size: 'lg' }
          ); // xl
          modalRef.componentInstance.rideId = this.clickedRide;
        }
      },
      error: (err) => {},
    });
  }

  getStreet(resultsOuter: RootObjectGeoApify) {
    return resultsOuter.results[0].street;
  }

  parseJson(str: string): any {
    return JSON.parse(str);
  }

  ngAfterViewInit() {
    // this.rideHistoryService = new RideHistoryService(new HttpClient());

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.rideHistoryService!.getHistorycalRides(
            this.sort.active,
            this.sort.direction,
            this.paginator.pageIndex,
            this.paginator.pageSize
          ).pipe(catchError(() => observableOf(null)));
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
        for (let c of this.data) {
          this.geoApifyService
            ?.getAddress(
              this.parseJson(this.parseJson(c.routeJSON)).waypoints[0]
                .location[0],
              this.parseJson(this.parseJson(c.routeJSON)).waypoints[0]
                .location[1]
            )
            .subscribe((results) => {
              c.startAddress =
                results.results[0].street +
                ' ' +
                results.results[0].housenumber;
            });
          let lastIndex =
            this.parseJson(this.parseJson(c.routeJSON)).waypoints.length - 1;
          this.geoApifyService
            ?.getAddress(
              this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex]
                .location[0],
              this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex]
                .location[1]
            )
            .subscribe((results) => {
              c.endAddress =
                results.results[0].street +
                ' ' +
                results.results[0].housenumber;
            });
        }
      });
  }
}
