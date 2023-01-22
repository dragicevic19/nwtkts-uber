import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideHistoryDetailedAdminModalComponent } from './ride-history-detailed-admin-modal.component';

describe('RideHistoryDetailedAdminModalComponent', () => {
  let component: RideHistoryDetailedAdminModalComponent;
  let fixture: ComponentFixture<RideHistoryDetailedAdminModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideHistoryDetailedAdminModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideHistoryDetailedAdminModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
