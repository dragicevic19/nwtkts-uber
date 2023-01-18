import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideHistoryDetailedUserModalComponent } from './ride-history-detailed-user-modal.component';

describe('RideHistoryDetailedUserModalComponent', () => {
  let component: RideHistoryDetailedUserModalComponent;
  let fixture: ComponentFixture<RideHistoryDetailedUserModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideHistoryDetailedUserModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideHistoryDetailedUserModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
