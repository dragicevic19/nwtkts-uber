import { Vehicle } from './Vehicle';

export interface Ride {
  id: number;
  routeJSON: string;
  rideStatus: string;
  vehicle: Vehicle;
}
