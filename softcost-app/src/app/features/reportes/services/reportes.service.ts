import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReporteGenerarRequest {
  nombreProyecto: string;
  cliente: string;
  fecha: string; 
  costosDirectos: number;
  costosIndirectos: number;
  duracionMeses: number;
  tamanoEquipo: number;
}

export interface ReporteGenerarResponse {
  status: string;
  reporte: string; 
}

@Injectable({ providedIn: 'root' })
export class ReportesService {
  private readonly baseApi = 'http://localhost:8083/api/v1';

  constructor(private readonly http: HttpClient) { }

  generar(payload: ReporteGenerarRequest): Observable<ReporteGenerarResponse> {
    // backend acepta snake_case y camelCase; map to snake_case to be explicit
    const body: any = {
      nombre_proyecto: payload.nombreProyecto,
      cliente: payload.cliente,
      fecha: payload.fecha,
      costos_directos: payload.costosDirectos,
      costos_indirectos: payload.costosIndirectos,
      duracion_meses: payload.duracionMeses,
      tamano_equipo: payload.tamanoEquipo
    };

    // If the component included derived totals, forward them to the backend as snake_case
    if ((payload as any).costoTotal !== undefined) body.costo_total = (payload as any).costoTotal;
    if ((payload as any).costoMensual !== undefined) body.costo_mensual = (payload as any).costoMensual;
    if ((payload as any).costoPorPersona !== undefined) body.costo_por_persona = (payload as any).costoPorPersona;

    return this.http.post<ReporteGenerarResponse>(`${this.baseApi}/reportes/generar`, body);
  }

  // No hay comprobación de librería nativa aquí: la app usará la respuesta del
  // endpoint /reportes/generar para validar la conexión al servicio de reportes.
}
