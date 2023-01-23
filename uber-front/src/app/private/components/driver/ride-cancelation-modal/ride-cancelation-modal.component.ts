import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
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
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    console.log(this.rideId);
    // this.rideDeatilHistoryService
    // .getRideDetailsClient(this.rideId).subscribe({
    //   next: (res) => {
    //     this.rideDetails = res;
    //     console.log(this.rideDetails.price);
    //   },
    //   error: (err) => {
    //   },
    // });

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
    // ako je sve okej slanje na backend ocena
    if(this.form.valid) {
      console.log('On send valid');
    // const reviewData = new RatingDTO(
    //   this.form.value as RawFormValueReview,
    //   this.rideId
    // );
    // this.driverService.cancelRide(ride.id).subscribe({
    //   next: (res) => {

    //   },
    //   error: (err) => {

    //   }
    // });


    // this.reviewService.putReviews(reviewData).subscribe({
    //   next: (res) => {
    //     console.log('Poslato dobro');
    //     this.successSending(res);
    //   },
    //   error: (err) => {
    //     console.log('Nije poslato dobro');
    //     this.reviewError(err);
    //   },
    // });


    }
    else {
      ValidateForm.validateAllFormFields(this.form);
    }

    this.clearFields();
    this.closeModal();
  }

  successSending(res: any) {
    console.log('Usao u poslato sucess');
    this.toastr.success('Cancelation sucessfuly sent.');
  }

  reviewError(err: any) {
    if (err.status === 400) {
      // BAD_REQUEST
      console.log('error 400 client');
      this.toastr.error(
        'Please login with your email and password.\nOnly clients can sign-in with Facebook or Google',
        'Login Error'
      );
      console.log(err.message);
    } else if (err.status === 401) {
      // UNAUTHORIZED
      console.log('error 401 client');
      this.toastr.error(
        'Please login with your email and password.\nAnd then try to review rides.',
        'UNAUTHORIZED Error'
      );
      console.log(err.message);
    } else {
      console.log('General error');
      this.toastr.error('An unexpected error occurred');
      console.log(err.message);
    }
  }

  clearFields() {
    this.form.reset();
  }
}
