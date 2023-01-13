export class Route {
  legs: any;
  duration: number;
  distance: number;
  price: number;
  legsStr: string;
  startingLatitude: number;
  startingLongitude: number;
  endingLatitude: number;
  endingLongitude: number;

  constructor(route: any, routeJSON: any) {
    
    this.legs = route.legs;
    this.duration = Math.round(route.duration / 60);
    this.distance = Number((route.distance / 1000).toFixed(1));
    this.price = Number((this.distance * 1.2).toFixed(2));
    this.legsStr = JSON.stringify(route.legs);

    this.startingLatitude = routeJSON.waypoints[0].location[1];
    this.startingLongitude = routeJSON.waypoints[0].location[0];
    this.endingLatitude = routeJSON.waypoints[routeJSON.waypoints.length - 1].location[1];
    this.endingLongitude = routeJSON.waypoints[routeJSON.waypoints.length - 1].location[0];
  }
}
