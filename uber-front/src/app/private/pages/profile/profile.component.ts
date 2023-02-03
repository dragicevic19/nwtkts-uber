import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from '../../models/User';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user!: User;
  changePass = false;
  homePath = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.whoAmI().subscribe({
      next: (res) => {
        this.user = res as User;
        this.user.role = this.getRole(res.role);
        this.setHomePath();
      },
    });
  }

  setHomePath() {
    if (this.user.role === 'Client') this.homePath = '/uber';
    else if (this.user.role === 'Driver') this.homePath = '/uber/driver';
    else this.homePath = '/admin/allUsers ';
  }

  getRole(role: string): string {
    if (role === 'ROLE_CLIENT') return 'Client';
    if (role === 'ROLE_DRIVER') return 'Driver';
    if (role === 'ROLE_ADMIN') return 'Admin';
    return '';
  }

  changePasswordVisibiliy() {
    this.changePass = !this.changePass;
  }
}
