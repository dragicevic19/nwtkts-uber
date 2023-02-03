import { ChartColumn } from "./ChartColumn";

export class ChartGroup {

    public name: string;
    public series: ChartColumn[];

    constructor(name: string, series: ChartColumn[]) {
        this.name = name;
        this.series = series;
    }

}