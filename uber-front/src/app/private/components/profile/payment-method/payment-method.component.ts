import { Component } from '@angular/core';

@Component({
  selector: 'app-payment-method',
  templateUrl: './payment-method.component.html',
  styleUrls: ['./payment-method.component.scss']
})
export class PaymentMethodComponent {

  addPaymentOn = false;

  addPayment() {
    this.addPaymentOn = true;
  }

}
