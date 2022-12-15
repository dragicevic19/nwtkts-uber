import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AdditionalSignInInfoDTO } from '../dto/additionalSignInInfo';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl: string = "http://localhost:8080/client/"

  constructor(private http: HttpClient) { }

  sendAdditionalSignUpInfo(info: AdditionalSignInInfoDTO) {
    return this.http.post<any>(`${this.baseUrl}finishSignUp`, info)
  }
}
