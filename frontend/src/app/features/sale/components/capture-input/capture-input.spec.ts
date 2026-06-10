import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaptureInput } from './capture-input';

describe('CaptureInput', () => {
  let component: CaptureInput;
  let fixture: ComponentFixture<CaptureInput>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaptureInput],
    }).compileComponents();

    fixture = TestBed.createComponent(CaptureInput);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
