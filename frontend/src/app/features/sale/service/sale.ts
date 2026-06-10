import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateSaleRequest, SaleResponse } from '../models/sale.model';
import { Observable } from 'rxjs';

@Service()
export class SaleService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/sales';

  createSale(request: CreateSaleRequest): Observable<SaleResponse> {
    return this.http.post<SaleResponse>(this.apiUrl, request);
  }
}
