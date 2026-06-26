import { Component, inject, signal } from '@angular/core';
import { CaptureInput } from '../../components/capture-input/capture-input';
import { CartTable } from '../../components/cart-table/cart-table';
import { SaleSummary } from '../../components/sale-summary/sale-summary';
import { SaleResponse } from '../../models/sale.model';
import { PaymentModal } from '../../components/payment-modal/payment-modal';
import { RouterLink } from '@angular/router';
import { CartService } from '../../service/cart';

@Component({
  selector: 'app-cashier-page',
  imports: [CaptureInput, CartTable, SaleSummary, PaymentModal, RouterLink],
  templateUrl: './cashier-page.html',
  styleUrl: './cashier-page.css',
})
export class CashierPage {

  private cart = inject(CartService);

  modalOpen = signal(false);

  openModal(): void {
    this.modalOpen.set(true);
  }

  closeModal(): void {
    this.modalOpen.set(false);
  }

  onSaleCompleted(sale: SaleResponse): void {
    // Limpia la tabla tras la venta
    this.cart.clear();
    // Cierra el modal
    this.modalOpen.set(false);
    // Más adelante: aquí se mostrara el ticket
    console.log('Venta completada: ', sale);
  }
}
