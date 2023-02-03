import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RideService } from 'src/app/core/services/ride/ride.service';
import { RatingDTO, RawFormValueReview } from 'src/app/private/models/ride-history/RatingDTO';
import { RideHistoryDetailsClient } from 'src/app/private/models/ride-history/RideHistoryDetailsClient';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';
import { RideRatingService } from 'src/app/private/services/ride-rating.service';

const EMPTY_STAR_SRC = "assets/img/empty_star.png";
const FULL_STAR_SRC = "assets/img/star.png";

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent implements OnInit {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsClient;

  form!: FormGroup;

  starSource: string = EMPTY_STAR_SRC;
  favRoute = false;

  constructor(private activeModal: NgbActiveModal,
    private rideDeatilHistoryService: RideDetailHistoryService,
    private fb: FormBuilder,
    private reviewService: RideRatingService,
    private toastr: ToastrService,
    private rideService: RideService) { }

  ngOnInit() {
    this.rideDeatilHistoryService
      .getRideDetailsClient(this.rideId).subscribe({
        next: (res) => {
          this.rideDetails = res;
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
    // ako je sve okej slanje na backend ocena
    const reviewData = new RatingDTO(this.form.value as RawFormValueReview, this.rideId);
    this.reviewService.putReviews(reviewData).subscribe({
      next: () => {
        this.toastr.success("Review sucessfully sent.");
      },
      error: () => {
        this.toastr.error('An unexpected error occurred');
      },
    })
    this.clearFields();
  }

  clearFields() {
    this.form.reset();
  }

  starSelected() {
    if (!this.favRoute) {
      this.favRoute = !this.favRoute;
      this.addToFavoriteRoutes();
    }
    else {
      this.toastr.warning('You can remove favorite route from Favorite Routes page');
    }
  }

  addToFavoriteRoutes() {
    this.rideService.newFavRouteFromHistory(this.rideId).subscribe({
      next: () => {
        this.toastr.success('Route added to favorites!')
      },
      error: (err) => {
        console.log(err);
        this.toastr.error('Some error occurred...');
      }
    });
    this.starSource = FULL_STAR_SRC;
  }



}
