import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RatingDTO } from '../models/ride-history/RatingDTO';

@Injectable({
  providedIn: 'root'
})
export class RideRatingService {

  constructor(private httpClient: HttpClient) { }

  putReviews(reviewData: RatingDTO)  {
    const href = 'http://localhost:8080/api/ride/rating';
    console.log(reviewData);

    return this.httpClient.put<any>(href, reviewData);
  }



}
