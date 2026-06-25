import { Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { AdminProduct, ProductFormData } from '../../pages/models/admin-product.model';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { submit } from '@angular/forms/signals';

@Component({
  selector: 'app-product-form-modal',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './product-form-modal.html',
  styleUrl: './product-form-modal.css',
})
export class ProductFormModal {
  private fb = inject(FormBuilder);

  // El modal se abre/cierra desde el padre
  readonly open = input<boolean>(false);
  // Si recibe un producto, está en modo edición; si es null, modo creacion
  readonly product = input<AdminProduct | null>(null);
  // Error que viene del backend (ej. SKU duplicado), mostrado por la pagina
  readonly error = input<string>('');

  readonly closed = output<void>();
  readonly saved = output<{ id: number | null; data: ProductFormData }>();

  // Campos del formulario como signals
  sku = signal('');
  name = signal('');
  description = signal('');
  price = signal<number | null>(null);
  stock = signal<number | null>(null);
  active = signal(true);
  categoryId = signal<number | null>(null);
  errorMessage = signal('');

  // Categorías de prueba (coinciden con el seed del backend)
  protected readonly categories = [
    { id: 1, name: 'General' },
    { id: 2, name: 'Alimentos' },
    { id: 3, name: 'Electrónica' },
    { id: 4, name: 'Ropa' },
  ];

  // True si estamos editando un producto existente
  protected readonly editing = computed(() => this.product() !== null);

  // Formulario reactivo con validadores por campo
  protected readonly form = this.fb.group({
    sku: ['', [Validators.required, Validators.maxLength(20)]],
    name: ['', [Validators.required, Validators.maxLength(120)]],
    description: ['', [Validators.maxLength(255)]],
    price: [null as number | null, [Validators.required, Validators.min(1)]],
    stock: [null as number | null, [Validators.required, Validators.min(1)]],
    categoryId: [null as number | null],
    active: [true],
  });

  constructor() {
    // Rellena (editar) o limpia (crear) el formulario cuando cambia el producto de entrada
    effect(() => {
      const p = this.product();
      if (p) {
        this.form.reset({
          sku: p.sku,
          name: p.name,
          description: p.description,
          price: p.price,
          stock: p.stock,
          categoryId: p.categoryId,
          active: p.active,
        });
      } else {
        this.form.reset({
          sku: '',
          name: '',
          description: '',
          price: null,
          stock: null,
          categoryId: null,
          active: true,
        });
      }
    });
  }

  save(): void {
    // Si hay errores, marrca todos los campos como "tocados" para mostar los mensajes
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const v = this.form.getRawValue();
    const data: ProductFormData = {
      sku: v.sku!.trim(),
      name: v.name!.trim(),
      description: (v.description ?? '').trim(),
      price: v.price!,
      stock: v.stock!,
      categoryId: v.categoryId,
      active: v.active!,
    };

    this.saved.emit({ id: this.product()?.id ?? null, data });
  }

  cancel(): void {
    this.closed.emit();
  }

  // Helper para el template: ¿este campo tiene error y ya fue tocado?
  protected hasError(field: string, error: string): boolean {
    const control = this.form.get(field);
    return !!control && control.touched && control.hasError(error);
  }
}
