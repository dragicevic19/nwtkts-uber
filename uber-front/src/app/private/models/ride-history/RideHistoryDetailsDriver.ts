import { Client } from "./Client";

export interface RideHistoryDetailsDriver {
  id: number;
  startTime: Date;
  calculatedDuration: number;
  routeJSON: string;
  pickup: string;
  destination: string;
  price: number;

  clients: Client[];
}
