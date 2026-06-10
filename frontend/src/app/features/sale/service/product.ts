import { inject, Service } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Service()
export class ProductService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/products';

  search(query: string): Observable<Product[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params });
  }

  findBySku(sku: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/sku/${sku}`);
  }
}
