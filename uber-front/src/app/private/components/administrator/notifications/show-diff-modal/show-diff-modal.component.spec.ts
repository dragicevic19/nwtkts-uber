import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowDiffModalComponent } from './show-diff-modal.component';

describe('ShowDiffModalComponent', () => {
  let component: ShowDiffModalComponent;
  let fixture: ComponentFixture<ShowDiffModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowDiffModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowDiffModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
