import { Component } from '@angular/core';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.scss']
})
export class NavMenuComponent {
  menuItems = [
    { label: 'Inicio', route: '/' },
    { label: 'Aprendizaje', route: '/aprendizaje' },
    { label: 'Calculadora de Costos', route: '/calculadora' },
    { label: 'Presupuestos', route: '/presupuestos' },
    { label: 'Métodos de Estimación', route: '/metodos' },
    { label: 'Reportes', route: '/reportes' },
    { label: 'Simulación', route: '/simulacion' }
  ];
}
