import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MetodosRoutingModule } from './metodos-routing.module';
import { MetodosPageComponent } from './pages/metodos-page/metodos-page.component';

@NgModule({
  declarations: [MetodosPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    MetodosRoutingModule
  ]
})
export class MetodosModule { }
