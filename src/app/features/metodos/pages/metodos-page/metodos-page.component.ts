import { Component } from '@angular/core';
import { HorasEstimacionService, HorasResponse } from '../../services/horas-estimacion.service';

@Component({
  selector: 'app-metodos-page',
  templateUrl: './metodos-page.component.html',
  styleUrls: ['./metodos-page.component.scss']
})
export class MetodosPageComponent {
  tabs = [
    { id: 'horas', label: 'Horas Hombre' },
    { id: 'modulo', label: 'Costos por Módulo' },
    { id: 'cocomo', label: 'COCOMO Básico' }
  ];

  activeTab = 'horas';

  // Horas Hombre inputs (no calculations performed locally)
  numTareas = 10;
  horasPromedio = 40;
  tarifaHora = 25;

  // remote results only
  remoteTotalHoras: number | null = null;
  remoteCostoTotal: number | null = null;
  loadingRemote = false;
  remoteError: string | null = null;
  serviceConnected: boolean | null = null;

  selectTab(tabId: string): void {
    this.activeTab = tabId;
  }

  constructor(private horasService: HorasEstimacionService) {}

  calcularConServicio(): void {
    this.remoteError = null;
    this.serviceConnected = null;
    this.loadingRemote = true;
    this.remoteTotalHoras = null;
    this.remoteCostoTotal = null;

    // map local fields to external service params: tareas=numTareas, promedio=horasPromedio, tarifa=tarifaHora
    this.horasService.calcular(this.numTareas, this.horasPromedio, this.tarifaHora).subscribe({
      next: (res: HorasResponse) => {
        this.remoteTotalHoras = res.totalHoras;
        this.remoteCostoTotal = res.costoTotal;
        this.serviceConnected = true;
        this.loadingRemote = false;
      },
      error: (err) => {
        this.remoteError = err?.message || 'Error al conectar con el servicio de horas';
        this.serviceConnected = false;
        this.loadingRemote = false;
      }
    });
  }
}
