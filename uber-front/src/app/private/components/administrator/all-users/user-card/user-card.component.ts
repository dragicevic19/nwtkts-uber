import { Component, Input} from '@angular/core';
import { User } from 'src/app/private/models/User';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { EditModalComponent } from '../edit-modal/edit-modal.component';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent {

  constructor(
    private modalService: NgbModal, 
    private administratorService: AdministratorService,
    private toastr: ToastrService,
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

  deleteUser() {
    this.administratorService.deleteUser(this.user.id).subscribe(
      (response: void) => {
        this.toastr.success("User was successfuly deleted!");
        // this.getAllUsers();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}