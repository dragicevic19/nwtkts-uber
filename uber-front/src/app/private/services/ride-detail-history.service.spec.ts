import { TestBed } from '@angular/core/testing';

import { RideDetailHistoryService } from './ride-detail-history.service';

describe('RideDetailHistoryService', () => {
  let service: RideDetailHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RideDetailHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
