import { Component, inject } from '@angular/core';
import { CartService } from '../../service/cart';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-cart-table',
  imports: [DecimalPipe],
  templateUrl: './cart-table.html',
  styleUrl: './cart-table.css',
})
export class CartTable {
  protected cart = inject(CartService);

  increase(productId: number, current: number): void {
    this.cart.updateQuantity(productId, current + 1);
  }

  decrease(productId: number, current: number): void {
    this.cart.updateQuantity(productId, current - 1);
  }

  remove(productId: number): void {
    this.cart.removeProduct(productId);
  }
}
