import { Client } from "./Client";

export interface RideHistoryDetailsAdmin {
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
  clients: Client[];
}
