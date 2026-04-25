import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SimulacionRoutingModule } from './simulacion-routing.module';
import { SimulacionPageComponent } from './pages/simulacion-page/simulacion-page.component';

@NgModule({
  declarations: [SimulacionPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    SimulacionRoutingModule
  ]
})
export class SimulacionModule { }
