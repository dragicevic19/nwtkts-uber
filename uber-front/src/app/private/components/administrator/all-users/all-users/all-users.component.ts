import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.scss']
})


export class AllUsersComponent {

   // @ts-ignore
  @Input() users: User[];
   // @ts-ignore
  @Input() getAllUsers: Function;
   // @ts-ignore
  @Input() onOpenModal: Function;
   // @ts-ignore
  @Input() onUpdateUser: Function;
  // @ts-ignore
  @Input() editUser: User;
    // @ts-ignore
  @Input() administratorService: AdministratorService;
  // @ts-ignore
  public deleteUser: User;

  public searchUsers(key: string): void {
    console.log(key);
    const results: User[] = [];
    for (const users of this.users) {
      if (users.firstName.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || users.email.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || users.phone.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || users.role.toLowerCase().indexOf(key.toLowerCase()) !== -1) {
        results.push(users);
      }
    }
    this.users = results;
    if (results.length === 0 || !key) {
      this.getAllUsers();
    }
  }

}
