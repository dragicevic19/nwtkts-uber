import { Component, Input, OnInit } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})

export class MyProfileComponent {
  @Input() onOpenModal!: Function;
  @Input() adminUser!: User;
  @Input() editUser!: User;
  @Input() administratorService!: AdministratorService;
  @Input() onUpdateUser!: Function;
  @Input() getAllUsers!: Function;

  ngOnInit() {
    this.getAllUsers();
  }
}
