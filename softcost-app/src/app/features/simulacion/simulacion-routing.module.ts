import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SimulacionPageComponent } from './pages/simulacion-page/simulacion-page.component';

const routes: Routes = [
  { path: '', component: SimulacionPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SimulacionRoutingModule { }
