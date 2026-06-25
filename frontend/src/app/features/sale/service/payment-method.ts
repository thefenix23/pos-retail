import { inject, Service, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PaymentMethod } from '../models/payment-method.model';
import { Observable, tap } from 'rxjs';

@Service()
export class PaymentMethodService {

  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/payment-methods';

  // Cache en memoria de los métodos de pago
  private methodsSignal = signal<PaymentMethod[]>([]);
  readonly methods = this.methodsSignal.asReadonly();

  load(): Observable<PaymentMethod[]> {
    return this.http
      .get<PaymentMethod[]>(this.apiUrl)
      .pipe(tap(
        (list) => this.methodsSignal.set(list),
      ));
  }

  // Devuelve el nombre del método por id; "-" si no está cargando todavía
  nameOf(id: number): string {
    const found = this.methodsSignal()
      .find((m) => m.id === id);
    return found ? found.name : '-';
  }
}
