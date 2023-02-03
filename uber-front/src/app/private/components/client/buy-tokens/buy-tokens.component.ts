import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IClientAuthorizeCallbackData, ICreateOrderRequest, IPayPalConfig } from 'ngx-paypal';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from 'src/app/core/services/client/client.service';
import { ClientsWallet } from 'src/app/private/models/ClientsWallet';
import { PurchaseDetails } from 'src/app/private/models/PurchaseDetails';

@Component({
  selector: 'app-buy-tokens',
  templateUrl: './buy-tokens.component.html',
  styleUrls: ['./buy-tokens.component.scss']
})
export class BuyTokensComponent implements OnInit {
  @Input() clientsWallet!: ClientsWallet;
  @Output() clientsWalletChanged = new EventEmitter<ClientsWallet>();

  buyTokensForm!: FormGroup<any>;
  public payPalConfig?: IPayPalConfig;


  constructor(private fb: FormBuilder, private clientService: ClientService,
    private toastr: ToastrService) { }

  ngOnInit(): void {

    this.buyTokensForm = this.fb.group({
      amount:
        [
          '',
          [
            Validators.required,
            Validators.pattern(/^[0-9]*$/),
          ]
        ]
    });
    this.initConfig();
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'AbqLCEb0CfZqggrapLDoSuAf__KXWXc43pTtU44V0-muORcbTQkgY2qxWtXwCMEmZME6yL5m1ARXbeAt',
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        purchase_units: [
          {
            amount: {
              currency_code: 'USD',
              value: this.buyTokensForm.controls['amount'].value,
              breakdown: {
                item_total: {
                  currency_code: 'USD',
                  value: this.buyTokensForm.controls['amount'].value,
                }
              }
            },
            items: [
              {
                name: 'Uber Tokens',
                quantity: this.buyTokensForm.controls['amount'].value,
                category: 'DIGITAL_GOODS',
                unit_amount: {
                  currency_code: 'USD',
                  value: '1',
                },
              }
            ]
          }
        ]
      },
      advanced: {
        commit: 'true'
      },
      style: {
        label: 'paypal',
        layout: 'vertical',
      },
      onClientAuthorization: (data) => {
        this.sendToServer(data);
      },
    };
  }
  sendToServer(data: IClientAuthorizeCallbackData) {
    const purchaseDetails = new PurchaseDetails(data);
    this.clientService.buyTokens(purchaseDetails).subscribe({
      next: (res: ClientsWallet) => {
        this.toastr.success('Successfully bought ' + purchaseDetails.amount + ' Tokens!')
        this.clientsWallet = res;
        this.clientsWalletChanged.emit(res);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

}
