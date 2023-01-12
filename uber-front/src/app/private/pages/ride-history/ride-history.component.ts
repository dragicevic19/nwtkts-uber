
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Component, ViewChild, AfterViewInit} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, SortDirection} from '@angular/material/sort';
import {lastValueFrom, merge, Observable, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';


@Component({
  selector: 'app-ride-history',
  templateUrl: './ride-history.component.html',
  styleUrls: ['./ride-history.component.scss'],
})
export class RideHistoryComponent implements AfterViewInit {
  displayedColumns: string[] = ['startTime', 'calculatedDuration', 'startLocation', 'endLocation', 'price'];
  exampleDatabase!: ExampleHttpDatabase | null;
  exampleDatabaseGeoApi!: ExampleHttpDatabaseGeoApify | null;
  data: Content[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  clickedRowIndex = -1;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private _httpClient: HttpClient, private _httpBackend: HttpBackend) {
    this.exampleDatabaseGeoApi = new ExampleHttpDatabaseGeoApify(new HttpClient(_httpBackend));
  }

  setClickedRowIndex(i: number) {
    this.clickedRowIndex = i;
    console.log(this.clickedRowIndex);
  }

  getStreet(resultsOuter: RootObjectGeoApify){
    return resultsOuter.results[0].street;
  }

  parseJson(str: string): any {
    return JSON.parse(str);
  }

  ngAfterViewInit() {
    this.exampleDatabase = new ExampleHttpDatabase(this._httpClient);

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.exampleDatabase!.getRepoIssues(
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

          this.exampleDatabaseGeoApi?.getAddress(this.parseJson(this.parseJson(c.routeJSON)).waypoints[0].location[0], this.parseJson(this.parseJson(c.routeJSON)).waypoints[0].location[1])
          .subscribe( results => {
            c.startAddress = results.results[0].street + ' ' + results.results[0].housenumber;
          }
          );
          let lastIndex = this.parseJson(this.parseJson(c.routeJSON)).waypoints.length - 1;
          this.exampleDatabaseGeoApi?.getAddress(this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex].location[0], this.parseJson(this.parseJson(c.routeJSON)).waypoints[lastIndex].location[1])
          .subscribe( results => {
            c.endAddress = results.results[0].street + ' ' + results.results[0].housenumber;
          }
          );
        }
      });

      

  }
}

// moje ----------------
export interface Content {
  id: number;
  startTime: string;
  calculatedDuration: number;
  routeJSON: string;
  price: number;
  startAddress?: string;
  endAddress?: string;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Pageable {
  sort: Sort;
  offset: number;
  pageSize: number;
  pageNumber: number;
  unpaged: boolean;
  paged: boolean;
}

export interface Sort2 {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface RootObject {
  content: Content[];
  pageable: Pageable;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: Sort2;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

// moje ----------------

// podaci za geoapify POCETAK

export interface Datasource {
  sourcename: string;
  attribution: string;
  license: string;
  url: string;
}

export interface Timezone {
  name: string;
  offset_STD: string;
  offset_STD_seconds: number;
  offset_DST: string;
  offset_DST_seconds: number;
  abbreviation_STD: string;
  abbreviation_DST: string;
}

export interface Rank {
  importance: number;
  popularity: number;
}

export interface Bbox {
  lon1: number;
  lat1: number;
  lon2: number;
  lat2: number;
}

export interface Result {
  datasource: Datasource;
  country: string;
  country_code: string;
  state: string;
  county: string;
  city: string;
  postcode: string;
  district: string;
  suburb: string;
  quarter: string;
  street: string;
  housenumber: string;
  lon: number;
  lat: number;
  distance: number;
  result_type: string;
  formatted: string;
  address_line1: string;
  address_line2: string;
  timezone: Timezone;
  rank: Rank;
  place_id: string;
  bbox: Bbox;
}

export interface RootObjectGeoApify {
  results: Result[];
}

// podaci za geoapify KRAJ



/** An example database that the data source uses to retrieve data for the table. */
export class ExampleHttpDatabase {
  constructor(private _httpClient: HttpClient) {}

  getRepoIssues(sort: string, order: SortDirection, page: number, pageSize: number): Observable<RootObject> {
    const href = 'http://localhost:8080/api/ride/client/history';
    // const requestUrl = `&sort=${sort}&order=${order}&page=${ page + 1 }`;

    const requestUrl = `${href}?page=${page}&size=${pageSize}&sort=${sort}&order=${order}`;

    return this._httpClient.get<RootObject>(requestUrl);
  }
}

export class ExampleHttpDatabaseGeoApify {
  constructor(private _httpClient: HttpClient) {}

  getAddress(lon: number, lat: number) : Observable<RootObjectGeoApify> {
    const href = 'https://api.geoapify.com/v1/geocode/reverse';
    const apiKey = 'ec1643040f2a4efda83f6a6c20c3d2d4';

    const requestUrl = `${href}?lat=${lat}&lon=${lon}&format=json&apiKey=${apiKey}`;

    // console.log(lat);
    // console.log(lon);

    return this._httpClient.get<RootObjectGeoApify>(requestUrl);
  }
}

