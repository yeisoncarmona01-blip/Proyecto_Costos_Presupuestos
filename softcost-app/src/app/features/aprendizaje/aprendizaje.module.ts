import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AprendizajeRoutingModule } from './aprendizaje-routing.module';
import { AprendizajePageComponent } from './pages/aprendizaje-page/aprendizaje-page.component';

@NgModule({
  declarations: [AprendizajePageComponent],
  imports: [
    CommonModule,
    AprendizajeRoutingModule
  ]
})
export class AprendizajeModule { }
