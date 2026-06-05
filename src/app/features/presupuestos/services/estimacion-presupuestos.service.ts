import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';

export interface EstimacionPresupuestoRequest {
  nombreProyecto: string;
  duracionMeses: number;
  tamanoEquipo: number;
  salarioPromedio: number;
  infraestructuraMensual: number;
  licenciasMensuales: number;
  contingenciaPorcentaje: number;
}

export interface EstimacionPresupuestoResponse {
  nombreProyecto: string;
  costoPersonal: number;
  costoInfraestructura: number;
  costoLicencias: number;
  subtotal: number;
  contingenciaMonto: number;
  totalPresupuesto: number;
  porcentajePersonal: number;
  porcentajeInfraestructura: number;
  porcentajeLicencias: number;
  porcentajeContingencia: number;
}

interface EstimacionPresupuestoApiResponse {
  total: number;
  personal: number;
  nombre_proyecto: string;
  contingencia: number;
  licencias: number;
  infraestructura: number;
  subtotal: number;
}

@Injectable({
  providedIn: 'root'
})
export class EstimacionPresupuestosService {
  private readonly url = 'http://localhost:8081/api/v1/presupuesto/calcular';

  constructor(private readonly http: HttpClient) {}

  calcular(payload: EstimacionPresupuestoRequest): Observable<EstimacionPresupuestoResponse> {
    const params = new HttpParams({
      fromObject: {
        nombre_proyecto: payload.nombreProyecto,
        duracion_meses: String(payload.duracionMeses),
        tamano_equipo: String(payload.tamanoEquipo),
        salario_promedio_mensual: String(payload.salarioPromedio),
        infraestructura_mensual: String(payload.infraestructuraMensual),
        licencias_mensuales: String(payload.licenciasMensuales),
        contingencia_porcentaje: String(payload.contingenciaPorcentaje)
      }
    });

    return this.http.get<EstimacionPresupuestoApiResponse>(this.url, { params }).pipe(
      map((response) => ({
        nombreProyecto: response.nombre_proyecto,
        costoPersonal: response.personal,
        costoInfraestructura: response.infraestructura,
        costoLicencias: response.licencias,
        subtotal: response.subtotal,
        contingenciaMonto: response.contingencia,
        totalPresupuesto: response.total,
        porcentajePersonal: response.total ? (response.personal / response.total) * 100 : 0,
        porcentajeInfraestructura: response.total ? (response.infraestructura / response.total) * 100 : 0,
        porcentajeLicencias: response.total ? (response.licencias / response.total) * 100 : 0,
        porcentajeContingencia: response.total ? (response.contingencia / response.total) * 100 : 0
      }))
    );
  }
}