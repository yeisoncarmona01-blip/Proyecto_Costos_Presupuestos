import { Component } from '@angular/core';

@Component({
  selector: 'app-presupuestos-page',
  templateUrl: './presupuestos-page.component.html',
  styleUrls: ['./presupuestos-page.component.scss']
})
export class PresupuestosPageComponent {
  projectName = 'Ej: Sistema de Gestión';
  duration = 3;
  teamSize = 5;
  avgSalary = 3000;
  infraMonthly = 1000;
  licensesMonthly = 500;

  get personalCost(): number {
    return this.duration * this.teamSize * this.avgSalary;
  }

  get infraCost(): number {
    return this.duration * this.infraMonthly;
  }

  get licenseCost(): number {
    return this.duration * this.licensesMonthly;
  }

  get subtotal(): number {
    return this.personalCost + this.infraCost + this.licenseCost;
  }

  get contingency(): number {
    return this.subtotal * 0.15;
  }

  get totalBudget(): number {
    return this.subtotal + this.contingency;
  }

  get personalPercent(): number {
    return this.totalBudget ? (this.personalCost / this.totalBudget) * 100 : 0;
  }

  get infraPercent(): number {
    return this.totalBudget ? (this.infraCost / this.totalBudget) * 100 : 0;
  }

  get licensePercent(): number {
    return this.totalBudget ? (this.licenseCost / this.totalBudget) * 100 : 0;
  }

  get contingencyPercent(): number {
    return this.totalBudget ? (this.contingency / this.totalBudget) * 100 : 0;
  }

  get monthlyCostPerPerson(): number {
    return this.teamSize ? this.avgSalary * this.teamSize : 0;
  }
}
