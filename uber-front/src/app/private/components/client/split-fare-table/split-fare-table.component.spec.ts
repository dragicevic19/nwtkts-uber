import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplitFareTableComponent } from './split-fare-table.component';

describe('SplitFareTableComponent', () => {
  let component: SplitFareTableComponent;
  let fixture: ComponentFixture<SplitFareTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SplitFareTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SplitFareTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
