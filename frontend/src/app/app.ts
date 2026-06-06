import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Ping, PingService } from './services/ping';

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private pingService = inject(PingService);
  pings = signal<Ping[]>([]);
  error = signal<string>('');

  ngOnInit(): void {
    this.pingService.getPings().subscribe({
      next: (data) => this.pings.set(data),
      error: (err) => this.error.set('No se pudo conectar al backend: ' + err.message),
    });
  }
}
