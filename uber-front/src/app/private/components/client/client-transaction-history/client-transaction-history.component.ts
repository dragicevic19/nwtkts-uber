import { Component, Input } from '@angular/core';
import { Transaction } from 'src/app/private/models/Transaction';

@Component({
  selector: 'app-client-transaction-history',
  templateUrl: './client-transaction-history.component.html',
  styleUrls: ['./client-transaction-history.component.scss']
})
export class ClientTransactionHistoryComponent {
  @Input() transactionHistory!: Transaction[];

}
