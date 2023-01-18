
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Component, ViewChild, AfterViewInit} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, SortDirection} from '@angular/material/sort';
import { MdbModalRef, MdbModalService } from 'mdb-angular-ui-kit/modal';
import {lastValueFrom, merge, Observable, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import { RideHistoryDetailedUserModalComponent } from '../../components/rideHistory/ride-history-detailed-user-modal/ride-history-detailed-user-modal.component';
import { RootObjectGeoApify } from '../../models/geoapify/RootObjectGeoApify';
import { Content } from '../../models/ride-history/Content';
import { RootObject } from '../../models/ride-history/RootObject';
import { GeoApifyService } from '../../services/geo-apify.service';
import { RideHistoryService } from '../../services/ride-history.service';


@Component({
  selector: 'app-ride-history',
  templateUrl: './ride-history.component.html',
  styleUrls: ['./ride-history.component.scss'],
})
export class RideHistoryComponent implements AfterViewInit {
  displayedColumns: string[] = ['startTime', 'calculatedDuration', 'startLocation', 'endLocation', 'price'];
  // rideHistoryService!: RideHistoryService | null;
  // geoApifyService!: GeoApifyService | null;
  data: Content[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  clickedRide = -1;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  modalUserRef: MdbModalRef<RideHistoryDetailedUserModalComponent> | null = null;


  constructor(private rideHistoryService: RideHistoryService, 
    private geoApifyService: GeoApifyService,
    private modalService: MdbModalService) {
    //this.exampleDatabaseGeoApi = new GeoApifyService(new HttpClient(_httpBackend));
  }

  setClickedRideId(i: number) {
    this.clickedRide = i;
    console.log(this.clickedRide);

    let modalConfig = {
      data: {
        rideId: this.clickedRide,
        modalClass: 'modal-xl modal-dialog-scrollable modal-dialog-centered'
      }
    }
    this.modalUserRef = this.modalService.open(RideHistoryDetailedUserModalComponent, modalConfig);

  }

  getStreet(resultsOuter: RootObjectGeoApify){
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
          return this.rideHistoryService!.getRepoIssues(
            this.sort.active,
            this.sort.direction,
            this.paginator.pageIndex,
            this.paginator.pageSize
          ).pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
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
        }),
      )
      .subscribe(data => {
        this.data = data;
        for (let c of this.data) {

          this.geoApifyService?.getAddress(this.parseJson(this.parseJson(c.routeJSON)).waypoints[0].location[0], this.parseJson(this.parseJson(c.routeJSON)).waypoints[0].location[1])
          .subscribe( results => {
            c.startAddress = results.results[0].street + ' ' + results.results[0].housenumber;
          }
          );
          let lastIndex = this.parseJson(this.parseJson(c.routeJSON)).waypoints.length - 1;
          this.geoApifyService?.getAddress(this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex].location[0], this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex].location[1])
          .subscribe( results => {
            c.endAddress = results.results[0].street + ' ' + results.results[0].housenumber;
          }
          );
        }
      });

      

  }
}
