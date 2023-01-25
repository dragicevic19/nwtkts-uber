import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { DriverService } from 'src/app/core/services/driver/driver.service';
import { RideService } from 'src/app/core/services/ride/ride.service';
import { RideCancelation } from 'src/app/private/models/RideCancelation';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-ride-cancelation-modal',
  templateUrl: './ride-cancelation-modal.component.html',
  styleUrls: ['./ride-cancelation-modal.component.scss'],
})
export class RideCancelationModalComponent {
  @Input() rideId!: number;

  form!: FormGroup;

  constructor(
    private activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private driverService: DriverService,
  ) {}

  ngOnInit() {
    console.log(this.rideId);

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
    if(this.form.valid) {
      console.log('On send valid');
    const cancelationData = new RideCancelation(this.rideId, this.form.value.cancelationReason);
    this.driverService.cancelRide(cancelationData).subscribe({
      next: (res) => {
        this.successSending(res);
      },
      error: (err) => {
        this.reviewError(err);
      }
    });
    }
    else {
      ValidateForm.validateAllFormFields(this.form);
    }

    this.clearFields();
    this.closeModal();
  }

  successSending(res: any) {
    this.toastr.success('Ride sucessfuly cancelled.');
  }

  reviewError(err: any) {
    this.toastr.error(err.error);
  }

  clearFields() {
    this.form.reset();
  }
  
}
