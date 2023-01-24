import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
import { AllUsersComponent } from 'src/app/private/components/administrator/all-users/all-users/all-users.component';
import { AdminAddDriverComponent } from 'src/app/private/components/administrator/admin-add-driver/admin-add-driver.component';
@Component({
  selector: 'app-page',
  templateUrl: './administrator.component.html',
  styleUrls: ['./administrator.component.scss']
})

export class AdministratorComponent {
  public users: User[] = [];

constructor(public administratorService: AdministratorService){}
}

