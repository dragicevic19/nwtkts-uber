export interface Content {
    id: number;
    startTime: string;
    calculatedDuration: number;
    routeJSON: string;
    price: number;
    startAddress?: string;
    endAddress?: string;
  }