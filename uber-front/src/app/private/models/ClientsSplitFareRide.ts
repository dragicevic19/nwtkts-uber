export interface ClientsSplitFareRide {
    id: number;
    requestedBy: string;
    pickup: string;
    destination: string;
    pricePerPerson: number;
    rideStatus: string;
    clientIds: number[];
}