import { Component } from '@angular/core';

interface AccordionItem {
  title: string;
  content: string;
  open: boolean;
}

@Component({
  selector: 'app-aprendizaje-page',
  templateUrl: './aprendizaje-page.component.html',
  styleUrls: ['./aprendizaje-page.component.scss']
})
export class AprendizajePageComponent {
  tabs = [
    { id: 'basicos', label: 'Conceptos Básicos' },
    { id: 'factores', label: 'Factores de Costo' },
    { id: 'metricas', label: 'Métricas' },
    { id: 'practicas', label: 'Mejores Prácticas' }
  ];

  activeTab = 'basicos';

  accordionItems: AccordionItem[] = [
    {
      title: '¿Qué es la estimación de costos de software?',
      content: 'Es el proceso de proyectar recursos, tiempo y esfuerzo económico necesarios para desarrollar, probar, desplegar y mantener una solución de software.',
      open: true
    },
    {
      title: 'Persona-Mes (PM)',
      content: 'Unidad que representa el trabajo realizado por una persona en un mes. Se utiliza para traducir esfuerzo técnico a costo económico.',
      open: false
    },
    {
      title: 'KLOC (Miles de Líneas de Código)',
      content: 'Métrica histórica para estimar tamaño de software. Suele combinarse con factores de complejidad para obtener pronósticos más realistas.',
      open: false
    },
    {
      title: 'Tipos de costos en proyectos de software',
      content: 'Incluyen costos directos (equipo, licencias, infraestructura), costos indirectos (gestión, soporte, administración) y reservas para riesgos.',
      open: false
    }
  ];

  selectTab(tabId: string): void {
    this.activeTab = tabId;
  }

  toggleAccordion(index: number): void {
    this.accordionItems[index].open = !this.accordionItems[index].open;
  }
}
