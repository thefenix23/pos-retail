import { Routes } from '@angular/router';
import { CashierPage } from './features/sale/pages/cashier-page/cashier-page';
import {ProductPage} from "./features/product/pages/product-page/product-page";
import {MainLayout} from "./shared/layout/main-layout/main-layout";
import {DashboardPage} from "./features/dashboard/dashboard-page";
import { SalesPage } from './features/sale/pages/sales-page/sales-page';

export const routes: Routes = [
  // Grupo 1: pantallas administrativas Dentro del shell (sidebar + topbar)
  {
    path: '',
    component: MainLayout,
    children: [
      { path: '', component: DashboardPage },
      { path: 'products', component: ProductPage },
      { path: 'ventas', component: SalesPage },
    ],
  },

  // Grupo 2: cashier a PANTALLA COMPLETA, sin shell
  { path: 'pos', component: CashierPage },

  // Cualquier otra ruta vuelve al dashboard
  { path:'', redirectTo: '', pathMatch: 'full' },
];
