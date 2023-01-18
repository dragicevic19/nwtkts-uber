import { Client } from "./Client";

export interface RideHistoryDetailsDriver {
  id: number;
  startTime: Date;
  calculatedDuration: number;
  routeJSON: string;
  price: number;

  clients: Client[];
}
