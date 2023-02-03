export class RideCancelation {
  
  rideId: number;
  cancelationReason: string;

  constructor(rideId: number, cancelationReason: string) {
    this.rideId = rideId;
    this.cancelationReason = cancelationReason;
  }

}
