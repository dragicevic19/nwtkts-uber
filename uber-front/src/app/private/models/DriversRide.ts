export interface DriversRide {
    id: number;
    rideStatus: string;
    pickup: string;
    destination: string;
    driverId: number;
    clientIds: number[];
}