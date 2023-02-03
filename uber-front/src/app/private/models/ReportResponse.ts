import { ReportResponseForDay } from "./ReportResponseForDay";

export interface ReportResponse {
    list: ReportResponseForDay[];
    cumulativeSumOfNumberOfRides: number;
    cumulativeSumOfPrice: number;
    cumulativeSumOfDistance: number;
    averageNumberOfRides: number;
    averagePrice: number;
    averageDistance: number;
    }
    