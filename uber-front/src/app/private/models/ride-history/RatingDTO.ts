export class RatingDTO {
    rideId: number;
    driverRating: number;
    vehicleRating: number;

    constructor(formValue: RawFormValueReview, rideId: number) {
        this.rideId = rideId;
        this.driverRating = formValue.driverRating;
        this.vehicleRating = formValue.vehicleRating;
    }
}

export interface RawFormValueReview {
    driverRating: number;
    vehicleRating: number;
}