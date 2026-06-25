import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-topbar',
  imports: [FormsModule],
  templateUrl: './topbar.html',
  styleUrl: './topbar.css',
})
export class Topbar {

  // Texto del buscador (por ahora solo visual, se conectara a busqueda global despues)
  search = signal('');

  // Hora de ultima sincronización. Generico por ahora
  // Cuando exista el módulo de sincronización (Fase 3) vendra del estado real
  lastSync = signal(
    'Hoy' +
    new Date().toLocaleDateString(
      'es-MX',
      { hour: '2-digit', minute: '2-digit' }
    ),
  );
}
