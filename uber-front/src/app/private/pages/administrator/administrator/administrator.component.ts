import { Component } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
@Component({
  selector: 'app-page',
  templateUrl: './administrator.component.html',
  styleUrls: ['./administrator.component.scss']
})

export class AdministratorComponent {
  public users: User[] = [];

constructor(public administratorService: AdministratorService){}
}

