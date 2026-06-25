import { TestBed } from '@angular/core/testing';

import { ProductAdmin } from './product-admin';

describe('ProductAdmin', () => {
  let service: ProductAdmin;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductAdmin);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
