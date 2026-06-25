import { TestBed } from '@angular/core/testing';

import { SaleQuery } from './sale-query';

describe('SaleQuery', () => {
  let service: SaleQuery;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SaleQuery);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
