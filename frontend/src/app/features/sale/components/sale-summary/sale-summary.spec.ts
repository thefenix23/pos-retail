import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleSummary } from './sale-summary';

describe('SaleSummary', () => {
  let component: SaleSummary;
  let fixture: ComponentFixture<SaleSummary>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaleSummary],
    }).compileComponents();

    fixture = TestBed.createComponent(SaleSummary);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
