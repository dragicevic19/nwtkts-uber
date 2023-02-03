import { Component, Input} from '@angular/core';
import { User } from 'src/app/private/models/User';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { EditModalComponent } from '../edit-modal/edit-modal.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent {

  constructor(
    private modalService: NgbModal, 
    private router: Router,
    ) {}

  @Input() user!: User;

  editUser() {
    const modalRef = this.modalService.open(EditModalComponent,  { size: 'lg' });
    modalRef.componentInstance.user = this.user;
    // modalRef.closed.subscribe((user = ))
  }

  getRole(user: User) {
    if (user.role === 'ROLE_ADMIN') return 'Admin';
    else if (user.role === 'ROLE_CLIENT') return 'Client';
    else return 'Driver';
  }

  getDriverStatus() {
    if (this.user.driverActive) {return 'Active'}
    else {return 'Inactive'}
  }

  redirectToProfile() {
    this.router.navigate([`uber/profile/${this.user.id}`])
  }

}