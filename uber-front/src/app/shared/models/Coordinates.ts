import { LatLngExpression } from "leaflet";

export class Coordinates {
    coords: LatLngExpression | null;
    type: number;

    constructor(coords: LatLngExpression | null, type: number) {
        this.coords = coords;
        this.type = type;
    }
}