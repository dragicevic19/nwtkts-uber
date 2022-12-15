import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdditionalSignupInfoComponent } from './additional-signup-info.component';

describe('AdditionalSignupInfoComponent', () => {
  let component: AdditionalSignupInfoComponent;
  let fixture: ComponentFixture<AdditionalSignupInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdditionalSignupInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdditionalSignupInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
