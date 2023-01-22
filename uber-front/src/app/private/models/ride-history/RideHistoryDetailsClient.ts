export interface RideHistoryDetailsClient {
  id: number;
  startTime: Date;
  calculatedDuration: number;
  routeJSON: string;
  pickup: string;
  destination: string;
  price: number;

  driverEmail: string;
  driverFirstName: string;
  driverLastName: string;
  licencePlateNumber: string;

  driverRated: boolean;
  vehicleRated: boolean;
  driverRating: number;
  vehicleRating: number;
}
