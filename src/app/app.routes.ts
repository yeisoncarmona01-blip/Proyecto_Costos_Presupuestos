import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';

export const routes: Routes = [
  // Auth pages (sin topbar/nav, layout propio)
  {
    path: 'login',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  // Main app pages (con topbar + nav layout)
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule)
      },
      {
        path: 'aprendizaje',
        loadChildren: () => import('./features/aprendizaje/aprendizaje.module').then(m => m.AprendizajeModule)
      },
      {
        path: 'calculadora',
        loadChildren: () => import('./features/calculadora/calculadora.module').then(m => m.CalculadoraModule)
      },
      {
        path: 'presupuestos',
        loadChildren: () => import('./features/presupuestos/presupuestos.module').then(m => m.PresupuestosModule)
      },
      {
        path: 'metodos',
        loadChildren: () => import('./features/metodos/metodos.module').then(m => m.MetodosModule)
      },
      {
        path: 'reportes',
        loadChildren: () => import('./features/reportes/reportes.module').then(m => m.ReportesModule)
      },
      {
        path: 'simulacion',
        loadChildren: () => import('./features/simulacion/simulacion.module').then(m => m.SimulacionModule)
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
