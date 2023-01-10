import { Vehicle } from './Vehicle';

export interface Ride {
  id: number;
  routeJSON: string;
  rideStatus: number;
  vehicle: Vehicle;
}
