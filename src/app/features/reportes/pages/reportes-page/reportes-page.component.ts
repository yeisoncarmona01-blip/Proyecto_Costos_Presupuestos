import { Component } from '@angular/core';

@Component({
  selector: 'app-reportes-page',
  templateUrl: './reportes-page.component.html',
  styleUrls: ['./reportes-page.component.scss']
})
export class ReportesPageComponent {
  projectName = 'Sistema de Gestión Empresarial';
  client = 'Empresa ABC S.A.';
  reportDate = new Date().toLocaleDateString('es-CO');
  directCosts = 45000;
  indirectCosts = 12000;
  duration = 4;
  teamSize = 6;

  get totalCost(): number {
    return this.directCosts + this.indirectCosts;
  }

  get monthlyCost(): number {
    return this.duration ? this.totalCost / this.duration : 0;
  }

  get costPerPerson(): number {
    return this.teamSize ? this.totalCost / this.teamSize : 0;
  }
}
