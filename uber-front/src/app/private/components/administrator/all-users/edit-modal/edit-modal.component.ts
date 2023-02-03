import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { User } from 'src/app/private/models/User';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-edit-modal',
  templateUrl: './edit-modal.component.html',
  styleUrls: ['./edit-modal.component.scss'],
})
export class EditModalComponent {
  constructor (
    private administratorService: AdministratorService, 
    public activeModal: NgbActiveModal,
    private toastr: ToastrService
    ) {}

  @Input() user!: User;

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  public onUpdateUser(user: User): void {
    this.administratorService.updateUser(user).subscribe(
      (response: User) => {
        this.user = response;
        this.closeModal();
        this.toastr.success("Success!")
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

}
