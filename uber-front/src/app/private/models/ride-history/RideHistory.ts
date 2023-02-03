export interface RideHistory {
    id: number;
    startTime: string;
    calculatedDuration: number;
    routeJSON: string;
    pickup: string;
    destination: string;
    price: number;
    startAddress?: string;
    endAddress?: string;
  }