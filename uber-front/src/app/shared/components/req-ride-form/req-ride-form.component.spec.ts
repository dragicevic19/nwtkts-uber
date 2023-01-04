import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReqRideFormComponent } from './req-ride-form.component';

describe('ReqRideFormComponent', () => {
  let component: ReqRideFormComponent;
  let fixture: ComponentFixture<ReqRideFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReqRideFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReqRideFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
