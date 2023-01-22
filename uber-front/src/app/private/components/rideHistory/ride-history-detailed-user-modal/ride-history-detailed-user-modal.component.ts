import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
// import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { Observable } from 'rxjs';
import { RatingDTO, RawFormValueReview } from 'src/app/private/models/ride-history/RatingDTO';
import { RideHistoryDetailsClient } from 'src/app/private/models/ride-history/RideHistoryDetailsClient';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';
import { RideRatingService } from 'src/app/private/services/ride-rating.service';
import ValidateForm from 'src/app/shared/helpers/validateform';

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsClient;

  form!: FormGroup;

  constructor(private activeModal: NgbActiveModal, 
    private rideDeatilHistoryService: RideDetailHistoryService,
    private fb: FormBuilder,
    private reviewService: RideRatingService,
    private toastr: ToastrService) {}

  ngOnInit() {
    this.rideDeatilHistoryService
    .getRideDetailsClient(this.rideId).subscribe({
      next: (res) => {
        this.rideDetails = res;
        console.log(this.rideDetails.price);
      },
      error: (err) => {
      },
    });

    this.form = this.fb.group({
      driverRating: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      vehicleRating: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

  }

  closeModal() {
    this.activeModal.close('Modal Closed');
  }

  onSend() {
    console.log(this.form.value.driverRating);
    console.log(this.form.value.vehicleRating);

    // ako je sve okej slanje na backend ocena
    // if(this.form.valid) {
      console.log("On send valid");
      const reviewData = new RatingDTO(this.form.value as RawFormValueReview, this.rideId);
      this.reviewService.putReviews(reviewData).subscribe({
        next: (res) => {
          console.log("Poslato dobro");
          this.successSending(res);
        },
        error: (err) => {
          console.log("Nije poslato dobro")
          this.reviewError(err);
        },
      })
    // }
    // else {
    //   ValidateForm.validateAllFormFields(this.form);
    // }
    
    // reakcija na pozitivan odgovor
    // reakcija na negativan odgovor
    this.clearFields();
  }
  
  successSending(res: any) {
    console.log("Usao u poslato sucess")
    this.toastr.success("Review sucessfuly sent.");
  }

  reviewError(err: any) {
    if(err.status === 400) {          // BAD_REQUEST
      console.log("error 400 client");
      this.toastr.error(
        'Please login with your email and password.\nOnly clients can sign-in with Facebook or Google',
        'Login Error'
      );
      console.log(err.message);
    }
    else if (err.status === 401) {    // UNAUTHORIZED
      console.log("error 401 client");
      this.toastr.error(
        'Please login with your email and password.\nAnd then try to review rides.',
        'UNAUTHORIZED Error'
      );
      console.log(err.message);
    }
    else {
      console.log("General error");
      this.toastr.error('An unexpected error occurred');
      console.log(err.message);
    }
  }

  clearFields() {
    this.form.reset();
  }

}
