export class Route {
  legs: any;
  duration: number;
  distance: number;
  price: number;

  constructor(route: any) {
    this.legs = route.legs;
    this.duration = Math.round(route.duration / 60);
    this.distance = Number((route.distance / 1000).toFixed(1));
    this.price = Number((this.distance * 1.2).toFixed(2));
  }
}
