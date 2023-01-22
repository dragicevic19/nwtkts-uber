import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientTransactionHistoryComponent } from './client-transaction-history.component';

describe('ClientTransactionHistoryComponent', () => {
  let component: ClientTransactionHistoryComponent;
  let fixture: ComponentFixture<ClientTransactionHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientTransactionHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientTransactionHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
