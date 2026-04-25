import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PresupuestosRoutingModule } from './presupuestos-routing.module';
import { PresupuestosPageComponent } from './pages/presupuestos-page/presupuestos-page.component';

@NgModule({
  declarations: [PresupuestosPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    PresupuestosRoutingModule
  ]
})
export class PresupuestosModule { }
