import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ProductAdminService } from '../../service/product-admin';
import { AdminProduct, ProductFormData } from '../models/admin-product.model';
import { FormsModule } from '@angular/forms';
import { ProductFormModal } from '../../components/product-form-modal/product-form-modal';
import { DecimalPipe } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-product-page',
  imports: [FormsModule, ProductFormModal, DecimalPipe],
  templateUrl: './product-page.html',
  styleUrl: './product-page.css',
})
export class ProductPage implements OnInit {
  protected admin = inject(ProductAdminService);

  query = signal('');
  modalOpen = signal(false);
  selectedProduct = signal<AdminProduct | null>(null);

  // Error que se pasa el modal (ej. SKU duplicado)
  modalError = signal('');

  // Lista filtrada según el buscador
  protected readonly filtered = computed(() => {
    const term = this.query().trim().toLowerCase();
    const all = this.admin.products();
    if (!term) return all;
    return all.filter(
      (p) => p.name.toLowerCase().includes(term) || p.sku.toLowerCase().includes(term),
    );
  });

  // Carga inicial de la lista al abrir la pantalla
  ngOnInit(): void {
    this.admin.load().subscribe({
      error: (err) => console.error('Error al cargar productos', err)
    });
  }

  openCreate(): void {
    this.selectedProduct.set(null);
    this.modalError.set('');
    this.modalOpen.set(true);
  }

  openEdit(product: AdminProduct): void {
    this.selectedProduct.set(product);
    this.modalError.set('');
    this.modalOpen.set(true);
  }

  closeModal(): void {
    this.modalOpen.set(false);
    this.selectedProduct.set(null);
    this.modalError.set('');
  }

  onSaved(event: { id: number | null, data: ProductFormData }): void {
    const esNuevo = event.id === null;
    const op$ = esNuevo
            ? this.admin.create(event.data)
            : this.admin.update(event.id!, event.data);

    op$.subscribe({
      next: () => {
        // Recarga la lista para reflejar el cambio en la tabla
        this.admin.load().subscribe();
        this.closeModal();

        // Ventana de éxito con palomita verde
        Swal.fire({
          icon: 'success',
          title: esNuevo ? '¡Producto registrado!' : '¡Producto actualizado!',
          text: esNuevo
          ? 'El producto se agregó al catálogo correctamente.'
              : 'Los cambios se guardaron correctamente',
          timer: 3000, // Se cierra sola en 2 segundos
          timerProgressBar: true, // Barra que muestra el tiempo restante
          showConfirmButton: false, // oculta el boton "Aceptar"
        });
      },
      error: (err) => {
        // 409 = SKU duplicado, 400 = validación
        const msg = err?.detail || 'No se pudo guardar el producto';
        this.modalError.set(msg);
      },
    });
  }

  confirmDelete(product: AdminProduct): void {
    Swal.fire({
      icon: 'warning',
      title: '¿Eliminar producto?',
      text: `"${product.name}" se eliminará permanentemente.`,
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#64748b',
    }).then((result) => {
      if (result.isConfirmed) {
        this.admin.delete(product.id).subscribe({
          next: () => {
            this.admin.load().subscribe();
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'El producto se eliminó del catálogo.',
              timer: 3000,
              timerProgressBar: true,
            });
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar el producto.',
              timer: 3000,
              timerProgressBar: true,
            });
          },
        });
      }
    });
  }
}
