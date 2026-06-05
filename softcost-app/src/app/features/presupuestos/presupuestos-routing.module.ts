import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PresupuestosPageComponent } from './pages/presupuestos-page/presupuestos-page.component';

const routes: Routes = [
  { path: '', component: PresupuestosPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PresupuestosRoutingModule { }
