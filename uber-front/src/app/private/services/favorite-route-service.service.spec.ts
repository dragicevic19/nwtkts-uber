import { TestBed } from '@angular/core/testing';

import { FavoriteRouteServiceService } from './favorite-route-service.service';

describe('FavoriteRouteServiceService', () => {
  let service: FavoriteRouteServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FavoriteRouteServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
