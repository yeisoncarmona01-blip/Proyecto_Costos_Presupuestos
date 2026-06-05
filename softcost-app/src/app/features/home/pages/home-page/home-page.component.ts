import { Component } from '@angular/core';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent {
  tools = [
    {
      number: '01',
      title: 'Calculadora de Costos',
      description: 'Calcula costos directos e indirectos de tu proyecto.',
      route: '/calculadora',
      accent: '#1268f2',
      accentSoft: '#dbe9ff'
    },
    {
      number: '02',
      title: 'Estimación de Presupuestos',
      description: 'Estima el presupuesto total de desarrollo con datos reales.',
      route: '/presupuestos',
      accent: '#14986c',
      accentSoft: '#d7f8ec'
    },
    {
      number: '03',
      title: 'Métodos de Estimación',
      description: 'Aplica horas hombre, puntos de función y modelos de referencia.',
      route: '/metodos',
      accent: '#5d38c2',
      accentSoft: '#ece5ff'
    },
    {
      number: '04',
      title: 'Reportes Automáticos',
      description: 'Genera reportes claros para presentar resultados y avances.',
      route: '/reportes',
      accent: '#ef7f18',
      accentSoft: '#ffeedb'
    },
    {
      number: '05',
      title: 'Simulación de Escenarios',
      description: 'Compara escenarios financieros y evalúa impacto en el presupuesto.',
      route: '/simulacion',
      accent: '#c23d77',
      accentSoft: '#ffe4ef'
    }
  ];

  features = [
    'Cálculo automatizado de costos directos e indirectos.',
    'Métodos de estimación reconocidos en la industria.',
    'Reportes listos para presentación académica o profesional.',
    'Simulaciones para analizar riesgos y sensibilidad.'
  ];
}
