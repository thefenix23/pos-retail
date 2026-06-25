import { inject, Service, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SaleSummary } from '../components/sale-summary/sale-summary';
import { Observable, tap } from 'rxjs';
import { SaleDetail } from '../models/SaleDetail-model';
import { SaleSummaryModel } from '../models/SaleSummary-model';

@Service()
export class SaleQueryService {

  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/sales';

  // Lista de ventas en memoria (signal) para la tabla
  private salesSignal = signal<SaleSummaryModel[]>([]);
  readonly sales = this.salesSignal.asReadonly();

  // Carga la lista (reciénte -> antigua, el backend ya ordena)
  load(): Observable<SaleSummaryModel[]> {
    return this.http
      .get<SaleSummaryModel[]>(this.apiUrl)
      .pipe(tap(
        (list) => this.salesSignal.set(list),
      ));
  }

  // Detalle de una venta (con nombres de producto)
  getById(id: number): Observable<SaleDetail> {
    return this.http.get<SaleDetail>(`${this.apiUrl}/${id}`);
  }
}
