import { Component, Input} from '@angular/core';
import {
  NgbDate,
  NgbCalendar,
  NgbDatepickerModule,
} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { NgbAlertModule, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ReportDTO } from '../../models/ReportDTO';
import { ChartServiceService } from '../../services/chart-service.service';
import { ReportResponse } from '../../models/ReportResponse';
import { ChartGroup } from '../../models/ChartGroup';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
})
export class ReportsComponent {

  @Input() user: number | null = null;

  model!: NgbDateStruct;

  hoveredDate: NgbDate | null = null;

  fromDate: NgbDate | null = null;
  toDate: NgbDate | null = null;

  data: ChartGroup[] = [];
  dataFullResponse: ReportResponse | undefined;

  currentDate = new Date();
  ngbDateCurrent : NgbDate = new NgbDate(this.currentDate.getUTCFullYear(), this.currentDate.getUTCMonth() + 1, this.currentDate.getUTCDate());

  constructor(
    calendar: NgbCalendar,
    private chartService: ChartServiceService,
    private toastr: ToastrService,
  ) {}

  generate() {
    if (this.fromDate == null) {
      this.toastr.error('From Date not selected!');
    }
    else if (this.toDate == null) {
      this.toastr.error('To Date not selected!');
    }
    else if (this.fromDate.after(this.toDate)) {
      this.toastr.error('From date is after To date!');
    }
    else if (this.toDate.after(this.ngbDateCurrent)) {
      this.toastr.error('To date needs to be before current date!');
    }
    else if (this.fromDate != null && this.toDate != null) {
      let dto: ReportDTO = new ReportDTO(
        new Date(this.fromDate?.year, this.fromDate?.month - 1, this.fromDate?.day + 1),
        new Date(this.toDate?.year, this.toDate?.month - 1, this.toDate?.day + 1),
        this.user
      );
      this.chartService.geChartData(dto).subscribe({
        next: (res: ReportResponse) => {
          this.toastr.success("Generating report.");
          this.dataFullResponse = res;
          this.data = res.list.map((el) => ({
            name: el.date,
            series: [
              { name: 'Number of rides', value: el.numberOfRides },
              { name: 'Price', value: el.price },
              { name: 'Distance', value: el.distance },
            ],
          }));
        },
        error: (err) => {
          console.log(err);
          this.toastr.error('An unexpected error occurred');
        },
      });
    }
    else {
      this.toastr.error('An unexpected error occurred');
    }
  }

  onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return (
      this.fromDate &&
      !this.toDate &&
      this.hoveredDate &&
      date.after(this.fromDate) &&
      date.before(this.hoveredDate)
    );
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return (
      date.equals(this.fromDate) ||
      (this.toDate && date.equals(this.toDate)) ||
      this.isInside(date) ||
      this.isHovered(date)
    );
  }
}
