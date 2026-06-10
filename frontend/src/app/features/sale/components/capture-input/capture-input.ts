import { Component, ElementRef, inject, signal, viewChild } from '@angular/core';
import {ProductService} from '../../service/product';
import {CartService} from '../../service/cart';
import {Product} from '../../models/product.model';
import {FormsModule} from '@angular/forms';
import { debounceTime, distinctUntilChanged, of, Subject, switchMap } from 'rxjs';

@Component({
  selector: 'app-capture-input',
  imports: [FormsModule],
  templateUrl: './capture-input.html',
  styleUrl: './capture-input.css',
})
export class CaptureInput {
  private productService = inject(ProductService);
  private cartService = inject(CartService);

  // Referencia al input para controlar el foco
  private inputRef = viewChild<ElementRef<HTMLInputElement>>('captureField');

  code = signal('');
  message = signal('');
  results = signal<Product[]>([]);
  selectedIndex = signal(0);

  // Stream para la búsqueda en vivo con debounce
  private searchTerm$ = new Subject<string>();

  constructor() {
    // Búsqueda en vivo: espera 250ms tras dejar de teclear, evita repetir la misma busqueda
    this.searchTerm$
      .pipe(
        debounceTime(250),
        distinctUntilChanged(),
        switchMap((term) => {
          if (term.trim().length < 3) {
            return of([]);
          }
          return this.productService.search(term.trim());
        }),
      )
      .subscribe((products) => {
        this.results.set(products);
        this.selectedIndex.set(0);
        this.message.set('');
      });
  }

  ngAfterContentInit(): void {
    this.focusInput();
  }

  // Se llama cada vez que cambia el texto
  onInput(value: string): void {
    this.code.set(value);
    // Solo busca en vivo si hay texto con sentido de nombre (3+ caracteres)
    if (value.trim().length >= 3) {
      this.searchTerm$.next(value);
    } else {
      this.results.set([]);
    }
  }

  // Flecha abajo: baja la selección
  onArrowDown(): void {
    if (this.results().length === 0) return;
    const next = (this.selectedIndex() + 1) % this.results().length;
    this.selectedIndex.set(next);
  }

  // Flecha arriba: sube elección
  onArrowUp(): void {
    if (this.results().length === 0) return;
    const prev = (this.selectedIndex() - 1 + this.results().length) % this.results().length;
    this.selectedIndex.set(prev);
  }

  // Enter: agrega el seleccionado, o intenta SKU si no hay lista
  onEnter(): void {
    const term = this.code().trim();
    if (!term) return;

    // Si hay resultados en la lista, agrega el seleccionado
    if (this.results().length > 0) {
      const product = this.results()[this.selectedIndex()];
      this.addToCart(product);
      return;
    }

    // Si no hay lista, intenta por SKU exacto (caso escáner / código)
    this.productService.findBySku(term).subscribe({
      next: (prduct) => this.addToCart(prduct),
      error: () => this.message.set('No se encontró el producto: ' + term),
    });
  }

  // Click en un resultado con el mouse
  selectResult(product: Product): void {
    this.addToCart(product);
  }

  private searchByName(term: string): void {
    this.productService.search(term).subscribe({
      next: (products) => {
        if (products.length === 0) {
          this.message.set('No se encontró ningún producto');
          this.results.set([]);
        } else if (products.length === 1) {
          // Un solo resultado: agregra directo
          this.cartService.addProduct(products[0]);
          this.reset();
        } else {
          // Varios: mostrar para elegir
          this.results.set(products);
          this.message.set('');
        }
      },
      error: () => {
        this.message.set('Error al buscar el producto');
      },
    });
  }

  private reset(): void {
    this.code.set('');
    this.results.set([]);
    this.message.set('');
  }

  private focusInput() {
    this.inputRef()?.nativeElement.focus();
  }

  private addToCart(product: Product) {
    this.cartService.addProduct(product);
    this.reset();
    this.focusInput();
  }
}
