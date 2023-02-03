import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { Notification } from 'src/app/private/models/Notification';

@Component({
  selector: 'app-show-diff-modal',
  templateUrl: './show-diff-modal.component.html',
  styleUrls: ['./show-diff-modal.component.scss']
})
export class ShowDiffModalComponent {

  constructor(
    public activeModal: NgbActiveModal,
    private administratorService: AdministratorService,
    private toastr: ToastrService
  ) { }

  @Input() chosen!: Notification;

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  public acceptChanges() {
    this.chosen.status = 'accepted';
    this.administratorService.updateUser(this.chosen.newInfo).subscribe({
      next: () => {
        this.toastr.success('Successfuly accepted request for changing users personal information');
      },
      error: () => {
        this.toastr.error('An unexpected error occurred');
      },
    });

    this.administratorService.chagneNotificationStatus(this.chosen).subscribe({
      error: () => {
        this.toastr.error('An unexpected error while changing notification status occurred');
      },
    })
    this.closeModal();
  }

  public declineChanges() {
    this.chosen.status = 'declined';
    this.administratorService.chagneNotificationStatus(this.chosen).subscribe({
      next: () => {
        this.toastr.success('Successfuly declined request for changing users personal information');
      },
      error: () => {
        this.toastr.error('An unexpected error while changing notification status occurred');
      },
    })
    this.closeModal();
  }

}
