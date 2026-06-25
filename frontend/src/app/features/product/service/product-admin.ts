import { computed, inject, Service, signal } from '@angular/core';
import { AdminProduct, ProductFormData } from '../pages/models/admin-product.model';
import { list } from 'postcss';
import {HttpClient} from "@angular/common/http";
import { Observable, tap } from 'rxjs';

// Service de administración de productos.
@Service()
export class ProductAdminService {

  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/products';

  // Estado de la lista (se llena desde el backend)
  private readonly ProductsSignal = signal<AdminProduct[]>([]);
  readonly products = this.ProductsSignal.asReadonly();
  readonly count = computed(
      () => this.ProductsSignal().length,
  );

  // Carga todos los productos desde el backend y actualiza el signal
  load(): Observable<AdminProduct[]> {
    return this.http
        .get<AdminProduct[]>(this.apiUrl)
        .pipe(tap(
            (list) => this.ProductsSignal.set(list)
        ));
  }

  create(data: ProductFormData): Observable<AdminProduct> {
    return this.http.post<AdminProduct>(this.apiUrl, data);
  }

  update(id: number, data: ProductFormData): Observable<AdminProduct> {
    return this.http
        .put<AdminProduct>(
            `${this.apiUrl}/${id}`,
            data
        );
  }

  delete(id: number): Observable<void> {
    return this.http
        .delete<void>(`${this.apiUrl}/${id}`);
  }
}
