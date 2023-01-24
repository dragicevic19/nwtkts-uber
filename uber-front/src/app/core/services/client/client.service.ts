import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientsSplitFareRide } from 'src/app/private/models/ClientsSplitFareRide';
import { ClientsWallet } from 'src/app/private/models/ClientsWallet';
import { PurchaseDetails } from 'src/app/private/models/PurchaseDetails';
import { User } from 'src/app/private/models/User';
import { AdditionalSignInInfoDTO } from 'src/app/public/models/additionalSignInInfo';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { Route } from 'src/app/shared/models/Route';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl: string = "http://localhost:8080/client/"

  constructor(private http: HttpClient) { }

  sendAdditionalSignUpInfo(info: AdditionalSignInInfoDTO) {
    return this.http.post<any>(`${this.baseUrl}finishSignUp`, info)
  }

  findUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}userByEmail?email=${email}`);
  }

  getClientsWallet(): Observable<ClientsWallet> {
    return this.http.get<ClientsWallet>(`${this.baseUrl}fullWalletInfo`);
  }

  buyTokens(purchaseDetails: PurchaseDetails): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}buyTokens`, purchaseDetails);
  }

  getMySplitFareRequests(): Observable<ClientsSplitFareRide[]> {
    return this.http.get<ClientsSplitFareRide[]>(`${this.baseUrl}mySplitFareReqs`);
  }

  acceptSplitFareRequest(rideId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}acceptSplitFareReq`, rideId);
  }

  cancelSplitFare(rideId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}cancelSplitFareReq`, rideId);
  }

  getClientsFavRoutes(): Observable<FavRoute[]> {
    return this.http.get<FavRoute[]>(`${this.baseUrl}favRoutes`);
  }

  removeFavRoute(id: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}removeFavRoute`, id);
  }


}
