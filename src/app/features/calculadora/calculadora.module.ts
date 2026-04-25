import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalculadoraRoutingModule } from './calculadora-routing.module';
import { CalculadoraPageComponent } from './pages/calculadora-page/calculadora-page.component';

@NgModule({
  declarations: [CalculadoraPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    CalculadoraRoutingModule
  ]
})
export class CalculadoraModule { }
