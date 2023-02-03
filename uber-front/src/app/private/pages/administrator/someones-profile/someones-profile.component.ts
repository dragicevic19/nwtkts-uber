import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-someones-profile',
  templateUrl: './someones-profile.component.html',
  styleUrls: ['./someones-profile.component.scss']
})
export class SomeonesProfileComponent implements OnInit {

  userId!: number;
  user!: User;
  changePass = false;
  showReports = false;

  constructor(
    private route: ActivatedRoute,
    private administratorService: AdministratorService,
    private toastr: ToastrService,
    private router: Router) {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      this.userId = Number(id);
    });
  }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.administratorService.getUser(this.userId).subscribe({
      next: (response: User) => {
        this.user = response;
        this.user.role = this.getRole(response.role);
        if (this.user.role === 'Admin') {
          this.router.navigate(['uber/profile']);
        }
      },
      error: () => {
        this.toastr.error("There is no user with this ID, redirecting...");
        this.router.navigate(['/admin/allUsers'])
      }
    });
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

  onShowReports() {
    this.showReports = !this.showReports;
  }

  blockUserClicked() {
    Swal.fire({
      title: `Are you sure you want to block ${this.user.firstName} ${this.user.lastName}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, block it!'
    }).then((result) => {
      if (result.value) {
        this.administratorService.blockUser(this.user).subscribe({
          next: () => {
            this.user.blocked = true;
          },
          error: () => {
            this.toastr.error("There is no user with this ID, redirecting...");
            this.router.navigate(['/admin/allUsers'])
          }
        });
      }
    })
  }

  unblockUserClicked() {
    Swal.fire({
      title: `Are you sure you want to unblock ${this.user.firstName} ${this.user.lastName}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, unblock it!'
    }).then((result) => {
      if (result.value) {
        this.administratorService.unblockUser(this.user).subscribe({
          next: () => {
            this.user.blocked = false;
          },
          error: () => {
            this.toastr.error("There is no user with this ID, redirecting...");
            this.router.navigate(['/admin/allUsers'])
          }
        });
      }
    })
  }

}
