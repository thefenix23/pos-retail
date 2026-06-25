import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { SaleQueryService } from '../../service/sale-query';
import { PaymentMethodService } from '../../service/payment-method';
import { TicketService } from '../../service/ticket';
import { SaleDetail } from '../../models/SaleDetail-model';
import { SaleSummaryModel } from '../../models/SaleSummary-model';
import Swal from 'sweetalert2';
import { SaleResponse } from '../../models/sale.model';
import { CartItem } from '../../models/cart-item.model';
import { Product } from '../../models/product.model';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-sales-page',
  imports: [FormsModule, DatePipe, DecimalPipe],
  templateUrl: './sales-page.html',
  styleUrl: './sales-page.css',
})
export class SalesPage implements OnInit {
  protected salesQuery = inject(SaleQueryService);
  protected payments = inject(PaymentMethodService);
  private ticket = inject(TicketService);

  // Buscador por folio
  query = signal('');

  // Venta seleccionada /detalle a la derecha)
  selected = signal<SaleDetail | null>(null);
  loadingDetail = signal(false);

  // Lista filtrada por folio (id)
  protected readonly filtered = computed(() => {
    const term = this.query().trim();
    const all = this.salesQuery.sales();
    if (!term) return all;
    return all.filter((s) => s.id.toString().includes(term));
  });

  ngOnInit(): void {
    // Carga métodos de pago (para mostrar nombre en vez de id) y la lista de ventas
    this.payments.load().subscribe({
      error: (err) => console.error('Error al cargar métodos de pago', err),
    });

    this.salesQuery.load().subscribe({
      error: (err) => console.error('Error al cargar ventas', err),
    });
  }

  // Nombre del método de pago a partir del id
  protected methodName(id: number): string {
    return this.payments.nameOf(id);
  }

  // Al hacer clic en una fila, cargar el detalle
  selectSale(sale: SaleSummaryModel): void {
    this.loadingDetail.set(true);
    this.salesQuery.getById(sale.id).subscribe({
      next: (detail) => {
        this.selected.set(detail);
        this.loadingDetail.set(false);
      },
      error: (err) => {
        console.error('Error al cargar el detalle', err);
        this.loadingDetail.set(false);
        Swal.fire({
          icon: 'error',
          title: 'No se pudo cargar el detalle',
          text: 'Intenta de nuevo',
        });
      },
    });
  }

  // Reimprime el ticket de la venta seleccionada reusando TicketService
  reprint(): void {
    const detail = this.selected();
    if (!detail) return;

    // Mapea SaleDetail -> SaleResponse (lo que espera generate)
    const sale: SaleResponse = {
      id: detail.id,
      status: detail.status,
      total: detail.total,
      paymentMethodId: detail.paymentMethodId,
      items: detail.items.map((it) => ({
        productId: it.productId,
        quantity: it.quantity,
        unitPrice: it.unitPrice,
        subtotal: it.subtotal,
      })),
    };

    // Mapea los items del detalle -> CartItem[] (el ticket usa product.name, quantity, subtotal)
    const items: CartItem[] = detail.items.map((it) => {
      const product: Product = {
        id: it.productId,
        sku: '',
        name: it.productName,
        price: it.unitPrice,
        stock: 0,
        categoryId: null,
      };

      return {
        product,
        quantity: it.quantity,
        subtotal: it.subtotal,
      };
    });

    const methodName = this.payments.nameOf(detail.paymentMethodId);

    // En reimpresión no hay efectivo recibido: null -> el ticket omite la línea de cambio
    this.ticket.generate(sale, items, methodName, null);
  }

  protected folio(id: number): string {
    return id.toString().padStart(6, '0');
  }
}
