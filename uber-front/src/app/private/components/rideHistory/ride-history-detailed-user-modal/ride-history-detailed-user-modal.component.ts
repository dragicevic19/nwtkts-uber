import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
// import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { RatingDTO, RawFormValueReview } from 'src/app/private/models/ride-history/RatingDTO';
import { RideHistoryDetailsClient } from 'src/app/private/models/ride-history/RideHistoryDetailsClient';
import { FavoriteRouteServiceService } from 'src/app/private/services/favorite-route-service.service';
import { RideDetailHistoryService } from 'src/app/private/services/ride-detail-history.service';
import { RideRatingService } from 'src/app/private/services/ride-rating.service';

const EMPTY_STAR_SRC: string = "assets/img/empty_star.png";
const FULL_STAR_SRC: string = "assets/img/star.png";

@Component({
  selector: 'app-ride-history-detailed-user-modal',
  templateUrl: './ride-history-detailed-user-modal.component.html',
  styleUrls: ['./ride-history-detailed-user-modal.component.scss']
})
export class RideHistoryDetailedUserModalComponent {

  @Input() rideId!: number;

  rideDetails!: RideHistoryDetailsClient;

  form!: FormGroup;

  starSource: string = EMPTY_STAR_SRC;
  favRoute: boolean = false;

  constructor(private activeModal: NgbActiveModal, 
    private rideDeatilHistoryService: RideDetailHistoryService,
    private fb: FormBuilder,
    private reviewService: RideRatingService,
    private toastr: ToastrService,
    private routeService: FavoriteRouteServiceService) {}

  ngOnInit() {
    this.rideDeatilHistoryService
    .getRideDetailsClient(this.rideId).subscribe({
      next: (res) => {
        this.rideDetails = res;
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
    // ako je sve okej slanje na backend ocena
      const reviewData = new RatingDTO(this.form.value as RawFormValueReview, this.rideId);
      this.reviewService.putReviews(reviewData).subscribe({
        next: (res) => {
          this.successSending(res);
        },
        error: (err) => {
          this.reviewError(err);
        },
      })
    
    this.clearFields();
  }
  
  successSending(res: any) {
    this.toastr.success("Review sucessfuly sent.");
  }

  reviewError(err: any) {
    if(err.status === 400) {          // BAD_REQUEST
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

  starSelected() {
    this.favRoute = !this.favRoute;
    if (this.favRoute) {
      this.addToFavoriteRoutes();
    }
    else {
      this.toastr.warning('You can remove favorite route from Favorite Routes page');
    }
  }

  addToFavoriteRoutes() {  
    this.routeService.newFavRoute(this.rideId).subscribe({
      next: (res) => {
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
