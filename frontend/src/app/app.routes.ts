import { Routes } from '@angular/router';
import { CashierPage } from './features/sale/pages/cashier-page/cashier-page';

export const routes: Routes = [
  { path: '', component: CashierPage },
  { path: '**', redirectTo: '' },
];
