import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendRideListComponent } from './friend-ride-list.component';

describe('FriendRideListComponent', () => {
  let component: FriendRideListComponent;
  let fixture: ComponentFixture<FriendRideListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriendRideListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FriendRideListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
