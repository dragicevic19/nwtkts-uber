import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { UserService } from 'src/app/core/services/user/user.service';
import { User } from '../../models/User';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user!: User;

  constructor(
    private userService: UserService,
  ){}

  ngOnInit(): void {
    this.userService.whoAmI()
      .subscribe((res) => {
        this.user = res as User;
        this.user.role = this.getRole(res.role);
      });
  }


  getRole(role: String): string {
    if (role === 'ROLE_CLIENT') return 'Client';
    if (role === 'ROLE_DRIVER') return 'Driver';
    if (role === 'ROLE_ADMIN') return 'Admin';
    return '';
  }
}


