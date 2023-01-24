import { User } from "src/app/private/models/User";
import { Route } from "./Route";
import { VehicleType } from "./VehicleType";

export class RideRequest {
  selectedRoute: Route | null;
  vehicleType: VehicleType | null;
  pets: boolean;
  babies: boolean;
  addedFriends: User[];
  price: number;
  pricePerPerson: number;
  scheduled: string | null;
  addressValues = new Map<number, string>();
  addressValuesStr: string[];


  constructor() {
    this.selectedRoute = null;
    this.vehicleType = null;
    this.pets = false;
    this.babies = false;
    this.addedFriends = [];
    this.price = 0;
    this.pricePerPerson = 0;
    this.scheduled = null;
    this.addressValuesStr = [];
  }

  public setNewRoute(route: Route) {
    this.selectedRoute = route;
    this.price = (this.vehicleType) ? Number((this.vehicleType.additionalPrice + this.selectedRoute.price).toFixed(2)) : Number(this.selectedRoute.price.toFixed(2));
  }

  public setNewVehicleType(type: VehicleType) {
    this.vehicleType = type;
    this.price = Number((this.vehicleType.additionalPrice + this.selectedRoute!.price).toFixed(2));
  }

  public getPricePerPerson() {
    this.pricePerPerson = Number((this.price / (this.addedFriends.length + 1)).toFixed(2));
    return this.pricePerPerson;
  }

  public addFriend(user: User) {
    this.addedFriends.push(user);
  }

  public getRideRequestForRequest(): RideRequest {
    console.log(this);
    
    if (this.addressValues.size) {
      this.addressValues = new Map([...this.addressValues].sort());
      this.addressValuesStr = Array.from(this.addressValues.values());
      this.selectedRoute!.addressValuesStr = this.addressValuesStr;
    }
    return this;
  }
}