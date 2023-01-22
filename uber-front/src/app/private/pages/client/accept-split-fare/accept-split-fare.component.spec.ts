import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptSplitFareComponent } from './accept-split-fare.component';

describe('AcceptSplitFareComponent', () => {
  let component: AcceptSplitFareComponent;
  let fixture: ComponentFixture<AcceptSplitFareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptSplitFareComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptSplitFareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
