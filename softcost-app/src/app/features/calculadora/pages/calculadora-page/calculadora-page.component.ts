import { Component } from '@angular/core';

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
    return this.totalDirecto + this.totalIndirecto;
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
