import { Component, computed, inject, input, output, signal } from '@angular/core';
import { CartService } from '../../service/cart';
import { SaleService } from '../../service/sale';
import { CreateSaleRequest, SaleResponse } from '../../models/sale.model';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../service/ticket';

@Component({
  selector: 'app-payment-modal',
  imports: [DecimalPipe, FormsModule],
  templateUrl: './payment-modal.html',
  styleUrl: './payment-modal.css',
})
export class PaymentModal {
  protected cart = inject(CartService);
  private saleService = inject(SaleService);
  private ticketService = inject(TicketService);

  readonly open = input<boolean>(false);
  readonly closed = output<void>();
  readonly completed = output<SaleResponse>();

  paymentMethodId = signal(1);
  processing = signal(false);
  errorMessage = signal('');
  cashReceived = signal<number | null>(null);

  protected readonly paymentMethods = [
    { id: 1, name: 'Efectivo', icon: '💵' },
    { id: 2, name: 'Tarjeta de crédito', icon: '💳' },
    { id: 3, name: 'Tarjeta de débito', icon: '💳' },
    { id: 4, name: 'Transferencia', icon: '🏦' },
  ];
  protected readonly cashShortCuts = [100, 200, 500, 1000];

  protected readonly isCash = computed(() => this.paymentMethodId() === 1);

  // El cambio: lo recibido menos el total. si no alcanza o no hay monto, es null
  protected readonly change = computed(() => {
    const received = this.cashReceived();
    if (received == null || received < this.cart.total()) {
      return null;
    }
    return received - this.cart.total();
  });

  protected readonly canConfirm = computed(
    () => {
      if (this.cart.items().length === 0) return false;
      if (this.isCash()) {
        const received = this.cashReceived();
        return received !== null && received >= this.cart.total();
      }
      return false;
    }
  );

  selectMethod(id: number): void {
    this.paymentMethodId.set(id);
    this.errorMessage.set('');
    if (id !== 1) {
      this.cashReceived.set(null);
    }
  }

  setCash(amount: number): void {
    this.cashReceived.set(amount);
  }

  confirm(): void {
    if (!this.canConfirm()) {
      this.errorMessage.set('Revisa el método de pago o el efectivo recibido');
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

        // Genera y descarga el ticket PDF
        this.ticketService.generate(
          sale,
          this.cart.items(),
          this.paymentMethods.find(
            m => m.id === this.paymentMethodId(),
          )!.name,
          this.cashReceived()
        );

        this.completed.emit(sale);
      },
      error: (err) => {
        this.processing.set(false);
        this.errorMessage.set('Error al procesar: ' + (err.error.message || err.message));
      },
    });
  }

  cancel(): void {
    this.errorMessage.set('');
    this.cashReceived.set(null);
    this.paymentMethodId.set(1);
    this.closed.emit();
  }
}
