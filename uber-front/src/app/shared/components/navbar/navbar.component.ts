import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

  user?: User;

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.auth.whoAmI().subscribe({
      next: (res: User) => {
        this.user = res;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  loginClick() {
    window.location.href =
      window.location.protocol + '//' + window.location.host + '/login';
  }
}
