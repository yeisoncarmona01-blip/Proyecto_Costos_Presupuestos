import { Component } from '@angular/core';
import {
  EstimacionPresupuestosService,
  EstimacionPresupuestoRequest,
  EstimacionPresupuestoResponse
} from '../../services/estimacion-presupuestos.service';

@Component({
  selector: 'app-presupuestos-page',
  templateUrl: './presupuestos-page.component.html',
  styleUrls: ['./presupuestos-page.component.scss']
})
export class PresupuestosPageComponent {
  projectName = '';
  duration = 3;
  teamSize = 5;
  avgSalary = 3000;
  infraMonthly = 1000;
  licensesMonthly = 500;
  contingencyRate = 15;

  loading = false;
  errorMessage = '';

  estimation: EstimacionPresupuestoResponse = {
    nombreProyecto: '',
    costoPersonal: 0,
    costoInfraestructura: 0,
    costoLicencias: 0,
    subtotal: 0,
    contingenciaMonto: 0,
    totalPresupuesto: 0,
    porcentajePersonal: 0,
    porcentajeInfraestructura: 0,
    porcentajeLicencias: 0,
    porcentajeContingencia: 0
  };

  constructor(private readonly estimacionService: EstimacionPresupuestosService) {}

  get personalCost(): number {
    return this.estimation.costoPersonal;
  }

  get infraCost(): number {
    return this.estimation.costoInfraestructura;
  }

  get licenseCost(): number {
    return this.estimation.costoLicencias;
  }

  get subtotal(): number {
    return this.estimation.subtotal;
  }

  get contingency(): number {
    return this.estimation.contingenciaMonto;
  }

  get totalBudget(): number {
    return this.estimation.totalPresupuesto;
  }

  get personalPercent(): number {
    return this.estimation.porcentajePersonal;
  }

  get infraPercent(): number {
    return this.estimation.porcentajeInfraestructura;
  }

  get licensePercent(): number {
    return this.estimation.porcentajeLicencias;
  }

  get contingencyPercent(): number {
    return this.estimation.porcentajeContingencia;
  }

  get monthlyCostPerPerson(): number {
    return this.teamSize ? this.avgSalary * this.teamSize : 0;
  }

  calcularPresupuesto(): void {
    this.loading = true;
    this.errorMessage = '';

    const payload: EstimacionPresupuestoRequest = {
      nombreProyecto: this.projectName.trim(),
      duracionMeses: this.duration,
      tamanoEquipo: this.teamSize,
      salarioPromedio: this.avgSalary,
      infraestructuraMensual: this.infraMonthly,
      licenciasMensuales: this.licensesMonthly,
      contingenciaPorcentaje: this.contingencyRate
    };

    this.estimacionService.calcular(payload).subscribe({
      next: (response) => {
        this.estimation = response;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error?.error?.message ?? 'No se pudo conectar con la librería de estimación';
        this.loading = false;
      }
    });
  }
}
