import { Component } from '@angular/core';

interface Scenario {
  name: string;
  type: 'optimista' | 'realista' | 'pesimista';
  team: number;
  duration: number;
  salary: number;
  overhead: number;
  icon: string;
  tag?: string;
}

@Component({
  selector: 'app-simulacion-page',
  templateUrl: './simulacion-page.component.html',
  styleUrls: ['./simulacion-page.component.scss']
})
export class SimulacionPageComponent {
  scenarios: Scenario[] = [
    { name: 'Escenario Optimista', type: 'optimista', team: 4, duration: 3, salary: 2500, overhead: 10, icon: '↗' },
    { name: 'Escenario Realista', type: 'realista', team: 6, duration: 5, salary: 3000, overhead: 15, tag: 'Recomendado', icon: '' },
    { name: 'Escenario Pesimista', type: 'pesimista', team: 8, duration: 7, salary: 3500, overhead: 25, icon: '↘' }
  ];

  getCost(s: Scenario): number {
    const baseCost = s.team * s.duration * s.salary;
    return baseCost + (baseCost * s.overhead / 100);
  }

  get maxCost(): number {
    return Math.max(...this.scenarios.map(s => this.getCost(s)));
  }

  getBarHeight(s: Scenario): number {
    return this.maxCost ? (this.getCost(s) / this.maxCost) * 95 : 0;
  }

  get diffOptVsReal(): number {
    return Math.abs(this.getCost(this.scenarios[1]) - this.getCost(this.scenarios[0]));
  }

  get diffRealVsPes(): number {
    return Math.abs(this.getCost(this.scenarios[2]) - this.getCost(this.scenarios[1]));
  }

  get rangoTotal(): number {
    return Math.abs(this.getCost(this.scenarios[2]) - this.getCost(this.scenarios[0]));
  }

  getPercentDiff(diff: number, base: number): number {
    return base ? (diff / base) * 100 : 0;
  }
}
