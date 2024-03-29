import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RideRequest } from 'src/app/shared/models/RideRequest';

@Component({
  selector: 'app-schedule-ride',
  templateUrl: './schedule-ride.component.html',
  styleUrls: ['./schedule-ride.component.scss']
})
export class ScheduleRideComponent {


  @Input() rideRequest!: RideRequest;
  @Input() home?: boolean;
  @Output() goBackEvent = new EventEmitter<void>;

  times = this.calcTimes();

  timeClicked(time: string) {
    this.rideRequest.scheduled = time;
  }

  clear() {
    this.rideRequest.scheduled = null;
    this.goBackEvent.emit();
  }

  onSubmit() {
    this.goBackEvent.emit();
  }

  goBack() {
    this.goBackEvent.emit();
  }

  calcTimes() {
    const ret = [];
    const now = new Date();
    const currentTime = now.getTime();
    const lastDateTime = new Date(currentTime + 5 * 60 * 60 * 1000); // najvise 5 sati u napred moze da se zakaze

    let time = new Date(currentTime + 0 * 60 * 60 * 1000);
    while (time.getTime() <= lastDateTime.getTime()) {
      const timeStr = time.toLocaleTimeString(['it-IT'], {hour:'2-digit', minute:'2-digit'});
      ret.push(timeStr);
      time = new Date(time.getTime() + 1 * 60 * 1000); // +10 minuta je sledeci slot
    }

    return ret;
  }

}
