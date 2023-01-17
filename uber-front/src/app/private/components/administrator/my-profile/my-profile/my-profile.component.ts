import { Component, Input } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent {
  // @ts-ignore
  @Input() onOpenModal: Function;
  // @ts-ignore
  @Input() adminUser: User;
  // @ts-ignore
  @Input() editUser: User;
  // @ts-ignore
  @Input() administratorService: AdministratorService;
  // @ts-ignore
  @Input() onUpdateUser: Function;
  // @ts-ignore
  @Input() getAllUsers: Function;
}
