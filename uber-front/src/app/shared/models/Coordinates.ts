import { core } from "@angular/compiler";
import { LatLng, LatLngExpression } from "leaflet";

export class Coordinates {
    coords: LatLngExpression | null;
    type: CoordType;

    constructor(coords: LatLngExpression | null, type: CoordType) {
        this.coords = coords;
        this.type = type;
    }
}

export enum CoordType {
    PICKUP,
    DEST
};