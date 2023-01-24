import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-clients-balance-info',
  templateUrl: './clients-balance-info.component.html',
  styleUrls: ['./clients-balance-info.component.scss']
})
export class ClientsBalanceInfoComponent{

  @Input() title!: string;
  @Input() value!: number;
  @Input() link!: boolean;

}
