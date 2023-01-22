import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client/client.service';
import { ClientsWallet } from 'src/app/private/models/ClientsWallet';

@Component({
  selector: 'app-client-tokens',
  templateUrl: './client-tokens.component.html',
  styleUrls: ['./client-tokens.component.scss']
})
export class ClientTokensComponent implements OnInit {

  clientsWallet: ClientsWallet | null = null;

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.clientService.getClientsWallet().subscribe({
      next: (res: ClientsWallet) => {
        this.clientsWallet = res;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }



}
