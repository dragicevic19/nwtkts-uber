import { IClientAuthorizeCallbackData } from "ngx-paypal";

export class PurchaseDetails {
    status: string;
    amount: number;
    
    constructor(paypalResponse: IClientAuthorizeCallbackData) {
      this.status = paypalResponse.status;
      this.amount = Number(paypalResponse.purchase_units[0].amount.value);
    }
}