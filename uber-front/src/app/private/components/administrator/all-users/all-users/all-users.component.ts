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

  constructor (private administratorService: AdministratorService) {}

  adminUser!: User;
  editUser!: User;
  users!: User[];

  ngOnInit() {
    this.getAllUsers();
  }

  public getAllUsers(): void {
    var finalResponse: User[] = [];
      this.administratorService.getAllUsers().subscribe(
        (response: User[]) => {
          response.forEach(loopUser => {
            if (loopUser.role === "ROLE_ADMIN") {
              this.adminUser = loopUser;
            }
            else {
              finalResponse.push(loopUser);
            }
          });
          this.users = finalResponse;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
  }

  public searchUsers(key: string): void {
    const results: User[] = [];
    for (const users of this.users) {
      if (users.firstName.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || users.lastName.toLowerCase().indexOf(key.toLowerCase()) !== -1
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

  public getNumberOfUsers() {
    return this.users.length;
  }

}
