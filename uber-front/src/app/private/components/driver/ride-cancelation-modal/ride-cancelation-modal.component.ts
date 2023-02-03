import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { RideCancelation } from 'src/app/private/models/RideCancelation';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-ride-cancelation-modal',
  templateUrl: './ride-cancelation-modal.component.html',
  styleUrls: ['./ride-cancelation-modal.component.scss'],
})
export class RideCancelationModalComponent implements OnInit {
  @Input() rideId!: number;

  form!: FormGroup;

  constructor(
    private activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private driverService: DriverService,
  ) { }

  ngOnInit() {
    this.form = this.fb.group({
      cancelationReason: [
        '',
        [Validators.required],
      ],
    });
  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onSend() {
    if (this.form.valid) {
      const cancelationData = new RideCancelation(this.rideId, this.form.value.cancelationReason);
      this.driverService.cancelRide(cancelationData).subscribe({
        next: () => {
          this.toastr.success('Ride sucessfuly cancelled.');
        },
        error: (err) => {
          this.toastr.error(err.error);

        }
      });
    }
    else {
      ValidateForm.validateAllFormFields(this.form);
    }

    this.clearFields();
    this.closeModal();
  }

  clearFields() {
    this.form.reset();
  }

}
