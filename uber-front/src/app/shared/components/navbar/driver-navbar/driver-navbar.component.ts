import { Component, Input } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-driver-navbar',
  templateUrl: './driver-navbar.component.html',
  styleUrls: ['./driver-navbar.component.scss']
})
export class DriverNavbarComponent {

  @Input() user!: User;
  
  constructor(private auth: AuthService, private driverService: DriverService, private toastr: ToastrService) {
    
  }

  logout() {
    this.driverService.changeActive(false).subscribe({
      next: (res) => {
        this.user!.driverActive = false;
        this.auth.logout();
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    });
  }

  onCheckedActive(e: any) {
    e.preventDefault();
    this.driverService.changeActive(!this.user?.driverActive).subscribe({
      next: (res) => {
        this.user!.driverActive = !this.user?.driverActive;
      },
      error: (err) => {
        this.toastr.error(err.error);
      }
    });
  }

}
