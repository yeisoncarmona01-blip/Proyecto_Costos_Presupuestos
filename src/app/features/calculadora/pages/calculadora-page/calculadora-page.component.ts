import { Component } from '@angular/core';
import { CalculadoraService, CalculoCostosResponse } from '../../services/calculadora.service';

interface CostItem {
  name: string;
  value: number;
}

@Component({
  selector: 'app-calculadora-page',
  templateUrl: './calculadora-page.component.html',
  styleUrls: ['./calculadora-page.component.scss']
})
export class CalculadoraPageComponent {
  totalCalculado: number | null = null;
  cargando = false;
  errorServicio = false;

  constructor(private calculadoraService: CalculadoraService) {}

  directCosts: CostItem[] = [
    { name: 'Salarios desarrolladores', value: 0 }
  ];

  indirectCosts: CostItem[] = [
    { name: 'Servicios públicos', value: 0 }
  ];

  get totalDirecto(): number {
    return this.directCosts.reduce((sum, item) => sum + (item.value || 0), 0);
  }

  get totalIndirecto(): number {
    return this.indirectCosts.reduce((sum, item) => sum + (item.value || 0), 0);
  }

  get totalProyecto(): number {
    return this.totalCalculado !== null ? this.totalCalculado : (this.totalDirecto + this.totalIndirecto);
  }

  calcularConMicroservicio(): void {
    const request = {
      costosDirectos: this.directCosts,
      costosIndirectos: this.indirectCosts
    };

    this.cargando = true;
    this.errorServicio = false;
    
    this.calculadoraService.calcularTotal(request).subscribe({
      next: (response: CalculoCostosResponse) => {
        this.totalCalculado = response.totalProyecto;
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al calcular con el microservicio:', error);
        this.errorServicio = true;
        this.cargando = false;
        // Si falla, volvemos a mostrar la suma local temporal
        this.totalCalculado = null;
      }
    });
  }

  addDirect(): void {
    this.directCosts.push({ name: '', value: 0 });
  }

  addIndirect(): void {
    this.indirectCosts.push({ name: '', value: 0 });
  }

  removeDirect(index: number): void {
    this.directCosts.splice(index, 1);
  }

  removeIndirect(index: number): void {
    this.indirectCosts.splice(index, 1);
  }
}
