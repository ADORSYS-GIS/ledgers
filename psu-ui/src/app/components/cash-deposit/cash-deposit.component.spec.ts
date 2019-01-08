import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CashDepositComponent } from './cash-deposit.component';

describe('CashDepositComponent', () => {
  let component: CashDepositComponent;
  let fixture: ComponentFixture<CashDepositComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CashDepositComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CashDepositComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
