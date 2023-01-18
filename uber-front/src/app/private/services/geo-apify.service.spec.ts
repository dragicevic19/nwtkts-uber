import { TestBed } from '@angular/core/testing';

import { GeoApifyService } from './geo-apify.service';

describe('GeoApifyService', () => {
  let service: GeoApifyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GeoApifyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
