import { computed, Service, signal } from '@angular/core';
import { CartItem } from '../models/cart-item.model';
import { Product } from '../models/product.model';

@Service()
export class CartService {
  // Estado privado: la lista de items del carrito
  private readonly itemsSignal = signal<CartItem[]>([]);

  // Lectura pública del carrito (solo lectura desde fuera)
  readonly items = this.itemsSignal.asReadonly();

  // Total calculado automáticamente a partir de los items
  readonly total = computed(
    () => this.itemsSignal()
      .reduce(
        (sum, item) => sum + item.subtotal, 0
      )
  );

  // Cantidad total de unidades en el carrito
  readonly itemCount = computed(
    () => this.itemsSignal()
      .reduce(
        (sum, item) => sum + item.quantity, 0
      )
  );

  addProduct(product: Product): void {
    const current= this.itemsSignal();
    const existing = current.find(
      i => i.product.id === product.id
    );

    if (existing) {
      // Si el producto ya está, aumenta la cantidad
      this.updateQuantity(product.id, existing.quantity + 1);
    } else {
      // Si no está, lo agrega con cantidad 1
      const newItem: CartItem = {
        product,
        quantity: 1,
        subtotal: product.price,
      };
      this.itemsSignal.set(
        [
          ...current,
          newItem,
        ]
      )
    }
  }

  updateQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) {
      this.removeProduct(productId);
      return;
    }

    this.itemsSignal.update(
      items => items.map(
        item => item.product.id === productId
        ? {
          ...item,
            quantity,
            subtotal: item.product.price * quantity
          }
          : item
      )
    );
  }

  removeProduct(productId: number): void {
    this.itemsSignal.update(
      items => items.filter(
        item => item.product.id !== productId
      )
    );
  }

  clear(): void {
    this.itemsSignal.set([]);
  }
}
