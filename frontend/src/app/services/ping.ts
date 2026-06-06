import { inject, Injectable, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ping {
  id: number;
  msg: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root',
})

export class PingService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api';

  getPings(): Observable<Ping[]> {
    return this.http.get<Ping[]>(`${this.apiUrl}/ping`, {})
  }
}
