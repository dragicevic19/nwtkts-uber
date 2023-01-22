import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideHistoryDetailedDriverModalComponent } from './ride-history-detailed-driver-modal.component';

describe('RideHistoryDetailedDriverModalComponent', () => {
  let component: RideHistoryDetailedDriverModalComponent;
  let fixture: ComponentFixture<RideHistoryDetailedDriverModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideHistoryDetailedDriverModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideHistoryDetailedDriverModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
