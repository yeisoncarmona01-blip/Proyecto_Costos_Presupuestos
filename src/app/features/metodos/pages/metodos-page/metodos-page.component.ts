import { Component } from '@angular/core';

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

  // Horas Hombre
  numTareas = 10;
  horasPromedio = 40;
  tarifaHora = 25;

  get totalHoras(): number {
    return this.numTareas * this.horasPromedio;
  }

  get costoTotalHoras(): number {
    return this.totalHoras * this.tarifaHora;
  }

  selectTab(tabId: string): void {
    this.activeTab = tabId;
  }
}
