import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
import { AllUsersComponent } from 'src/app/private/components/administrator/all-users/all-users/all-users.component';
@Component({
  selector: 'app-page',
  templateUrl: './administrator.component.html',
  styleUrls: ['./administrator.component.scss']
})

export class AdministratorComponent implements OnInit{
public users: User[] = [];
 // @ts-ignore
public adminUser: User;
 // @ts-ignore
public deleteUser: User;
 // @ts-ignore
 public editUser: User;

constructor(public administratorService: AdministratorService){}
activeNavItem = 'allUsers';
ngOnInit() {
  this.getAllUsers();
  this.administratorService.users = this.users;
  this.administratorService.activeNavItem$.subscribe(activeNavItem => {
    this.activeNavItem = activeNavItem;
  });
}

public getAllUsers(): void {
  var finalResponse: User[] = [];
    this.administratorService.getAllUsers().subscribe(
      (response: User[]) => {
        response.forEach(loopUser => {
          console.log(loopUser);
          if (loopUser.role === "ROLE_ADMIN") {
            this.adminUser = loopUser;
          }
          else {
            finalResponse.push(loopUser);
          }
        });
        this.users = finalResponse;
        console.log(this.users);
        console.log(this.adminUser);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onOpenModal(user: User, mode: string): void {
    console.log("ovde sammm")
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      // button.setAttribute('data-target', '#addUserModal');
    }
    if (mode === 'edit') {
      this.editUser = user;
      button.setAttribute('data-target', '#updateUserModal');
      console.log(button);
    }
    if (mode === 'delete') {
      this.deleteUser = user;
      // button.setAttribute('data-target', '#deleteUserModal');
    }
     // @ts-ignore
    container.appendChild(button);
    console.log(container);
    button.click();
    // $('#modalId').modal('show')
  }

  public onUpdateUser(user: User): void {
    console.log(user);
    this.administratorService.updateUser(user).subscribe(
      (response: User) => {
        console.log(response);
        this.getAllUsers();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  // public onDeleteEmloyee(userID: number): void {
  //   this.administratorService.deleteUser(userID).subscribe(
  //     (response: void) => {
  //       console.log(response);
  //       this.getAllUsers();
  //     },
  //     (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   );
  // }

  


}

