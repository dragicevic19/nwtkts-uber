import { RideRequest } from "./RideRequest";
import { Route } from "./Route";

export class FavRoute {
    id? : number;
    name = '';
    selectedRoute: Route | null = null;
    pickup = '';
    destination = '';

    constructor(rideRequest: RideRequest) {
        this.selectedRoute = rideRequest.selectedRoute;
        this.pickup = rideRequest.addressValuesStr[0];
        this.destination = rideRequest.addressValuesStr[rideRequest.addressValuesStr.length - 1];
    }
}