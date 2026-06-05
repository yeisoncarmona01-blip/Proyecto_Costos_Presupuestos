import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MetodosPageComponent } from './pages/metodos-page/metodos-page.component';

const routes: Routes = [
  { path: '', component: MetodosPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MetodosRoutingModule { }
