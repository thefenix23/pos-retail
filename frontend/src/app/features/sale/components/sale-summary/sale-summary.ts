import { Component, inject, signal } from '@angular/core';
import { CartService } from '../../service/cart';
import { SaleService } from '../../service/sale';
import { CreateSaleRequest, SaleResponse } from '../../models/sale.model';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-sale-summary',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './sale-summary.html',
  styleUrl: './sale-summary.css',
})
export class SaleSummary {
  protected cart = inject(CartService);
  private saleService = inject(SaleService);

  paymentMethodId = signal(1);
  processing = signal(false);
  errorMessage = signal('');

  // Método de pago (coinciden con los IDs de la DB)
  protected readonly paymentMethods = [
    { id: 1, name: 'Efectivo' },
    { id: 2, name: 'Tarjeta de crédito' },
    { id: 3, name: 'Tarjeta de débito' },
    { id: 4, name: 'Transferencia' },
  ];

  checkout(): void {
    if (this.cart.items().length === 0) {
      this.errorMessage.set('No hay productos en la venta');
      return;
    }

    this.processing.set(true);
    this.errorMessage.set('');

    const request: CreateSaleRequest = {
      items: this.cart.items().map((item) => ({
        productId: item.product.id,
        quantity: item.quantity,
      })),
      paymentMethodId: this.paymentMethodId(),
    };

    this.saleService.createSale(request).subscribe({
      next: (sale: SaleResponse) => {
        this.processing.set(false);
        this.cart.clear();
        // Más adelante: mostrar el ticket. Por ahora limpia la venta.
      },
      error: (err) => {
        this.processing.set(false);
        this.errorMessage.set('Error al procesar la venta: ' + (err.error?.message || err.message));
      },
    });
  }

  cancel(): void {
    this.cart.clear();
    this.errorMessage.set('');
  }

  articulosLabel(): string {
    const count = this.cart.itemCount();
    return count == 1 ? '1 artículo' : `${count} artículos`;
  }
}
