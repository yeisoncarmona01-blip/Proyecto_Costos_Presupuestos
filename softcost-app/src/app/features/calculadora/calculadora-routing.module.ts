import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CalculadoraPageComponent } from './pages/calculadora-page/calculadora-page.component';

const routes: Routes = [
  { path: '', component: CalculadoraPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CalculadoraRoutingModule { }
