import { Component } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-administrator-navbar',
  templateUrl: './administrator-navbar.component.html',
  styleUrls: ['./administrator-navbar.component.scss']
})
export class AdministratorNavbarComponent {

  constructor (private administratorService: AdministratorService) {}
  public users: User[] = [];
  activeNavItem = 'allUsers';

  activate(navItem: string) {
    this.activeNavItem = navItem;
    this.administratorService.setActiveNavItem(navItem);
  }
}
