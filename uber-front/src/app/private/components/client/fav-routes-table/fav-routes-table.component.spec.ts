import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavRoutesTableComponent } from './fav-routes-table.component';

describe('FavRoutesTableComponent', () => {
  let component: FavRoutesTableComponent;
  let fixture: ComponentFixture<FavRoutesTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavRoutesTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavRoutesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
