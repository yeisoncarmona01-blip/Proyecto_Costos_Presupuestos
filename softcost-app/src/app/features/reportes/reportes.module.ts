import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportesRoutingModule } from './reportes-routing.module';
import { ReportesPageComponent } from './pages/reportes-page/reportes-page.component';

@NgModule({
  declarations: [ReportesPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReportesRoutingModule
  ]
})
export class ReportesModule { }
