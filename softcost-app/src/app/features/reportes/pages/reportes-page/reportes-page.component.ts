import { Component } from '@angular/core';
import { ReportesService, ReporteGenerarRequest, ReporteGenerarResponse } from '../../services/reportes.service';

@Component({
  selector: 'app-reportes-page',
  templateUrl: './reportes-page.component.html',
  styleUrls: ['./reportes-page.component.scss']
})
export class ReportesPageComponent {
  projectName = 'Sistema de Gestión Empresarial';
  client = 'Empresa ABC S.A.';
  reportDate = new Date().toLocaleDateString('es-CO');
  directCosts = 45000;
  indirectCosts = 12000;
  duration = 4;
  teamSize = 6;
  monthlyCost = 17500;
  costPerPerson = 11666.67;
  // remote report
  generating = false;
  reporteStatus: string | null = null;
  serviceConnected: boolean | null = null;
  // valores devueltos por el servidor
  serverTotal: number | null = null;
  serverMonthly: number | null = null;
  serverPerPerson: number | null = null;

  constructor(private reportesService: ReportesService) {}

  generarReporte(): void {
    this.generating = true;
    this.reporteStatus = null;
    this.serviceConnected = null;

    const payload: ReporteGenerarRequest & any = {
      nombreProyecto: this.projectName,
      cliente: this.client,
      fecha: this.reportDate,
      costosDirectos: this.directCosts,
      costosIndirectos: this.indirectCosts,
      costoTotal: Number(this.directCosts || 0) + Number(this.indirectCosts || 0),
      duracionMeses: this.duration,
      tamanoEquipo: this.teamSize,
      costoMensual: this.monthlyCost,
      costoPorPersona: this.costPerPerson
    };

    this.reportesService.generar(payload).subscribe({
      next: (res: ReporteGenerarResponse) => {
        this.reporteStatus = res.status;
        // intentar parsear el campo 'reporte' si es JSON string
        try {
          const parsed = typeof res.reporte === 'string' ? JSON.parse(res.reporte) : res.reporte;
          if (parsed && parsed.costos) {
            this.serverTotal = Number(parsed.costos.total) || null;
          }
          if (parsed && parsed.detalles) {
            this.serverMonthly = Number(parsed.detalles.costo_mensual) || null;
            this.serverPerPerson = Number(parsed.detalles.costo_por_persona) || null;
          }

          if (this.serverMonthly !== null) {
            this.monthlyCost = this.serverMonthly;
          }

          if (this.serverPerPerson !== null) {
            this.costPerPerson = this.serverPerPerson;
          }
        } catch (e) {
          // ignore parse errors
          this.serverTotal = null;
          this.serverMonthly = null;
          this.serverPerPerson = null;
        }
        // La respuesta del endpoint indica que el servicio de reportes respondió correctamente
        this.serviceConnected = true;
        this.generating = false;
      },
      error: (err) => {
        this.reporteStatus = 'error';
        console.error('Error generando reporte:', err);
        this.serviceConnected = false;
        this.generating = false;
      }
    });
  }
}
