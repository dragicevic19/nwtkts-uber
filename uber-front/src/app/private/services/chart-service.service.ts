import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReportDTO } from '../models/ReportDTO';
import { ReportResponse } from '../models/ReportResponse';

@Injectable({
  providedIn: 'root'
})
export class ChartServiceService {

  constructor(private httpClient: HttpClient) { }
  
  geChartData(dto: ReportDTO): Observable<ReportResponse> {
    const href = 'http://localhost:8080/api/ride/reports';

    return this.httpClient.post<ReportResponse>(href, dto);
  }

}
