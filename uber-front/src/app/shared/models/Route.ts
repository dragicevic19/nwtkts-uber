export class Route {
    legs: any;
    duration: number;
    distance: number;

    constructor(route: any) {
        this.legs = route.legs;
        this.duration = Math.ceil(route.duration / 60);
        this.distance = Number((route.distance / 1000).toFixed(1));
    }
}