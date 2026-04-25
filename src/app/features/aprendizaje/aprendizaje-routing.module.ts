import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AprendizajePageComponent } from './pages/aprendizaje-page/aprendizaje-page.component';

const routes: Routes = [
  { path: '', component: AprendizajePageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AprendizajeRoutingModule { }
