import { Component } from '@angular/core';
import { CaptureInput } from '../../components/capture-input/capture-input';
import { CartTable } from '../../components/cart-table/cart-table';
import { SaleSummary } from '../../components/sale-summary/sale-summary';

@Component({
  selector: 'app-cashier-page',
  imports: [CaptureInput, CartTable, SaleSummary],
  templateUrl: './cashier-page.html',
  styleUrl: './cashier-page.css',
})
export class CashierPage {}
