import { RideRequest } from "./RideRequest";
import { Route } from "./Route";

export class FavRoute {
    id? : number;
    name: string = '';
    selectedRoute: Route | null = null;
    pickup: string = '';
    destination: string = '';

    constructor(rideRequest: RideRequest) {
        this.selectedRoute = rideRequest.selectedRoute;
        this.pickup = rideRequest.addressValuesStr[0];
        this.destination = rideRequest.addressValuesStr[rideRequest.addressValuesStr.length - 1];
    }
}