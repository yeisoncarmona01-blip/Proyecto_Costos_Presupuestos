import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';

export interface CalculoCostosRequest {
  costosDirectos: { name: string, value: number }[];
  costosIndirectos: { name: string, value: number }[];
}

export interface CalculoCostosResponse {
  totalProyecto: number;
}

@Injectable({
  providedIn: 'root'
})
export class CalculadoraService {
  // TODO: ¡MUY IMPORTANTE! Cambia "localhost" por la IP pública de tu instancia de AWS (ej. http://3.80.XX.XX:8080/api/v1/costos)
  private apiUrl = 'http://18.117.12.26:8080/api/v1/costos';

  constructor(private http: HttpClient) { }

  calcularTotal(request: CalculoCostosRequest): Observable<CalculoCostosResponse> {
    // 0. PRIMERO: Llamamos a un endpoint para limpiar los datos viejos en Java
    return this.http.get(`${this.apiUrl}/limpiar`).pipe(
      switchMap(() => {
        // 1. Creamos las peticiones para agregar todos los costos directos
        const peticionesDirectos = request.costosDirectos.map(costo =>
          this.http.get(`${this.apiUrl}/agregar-directo?concepto=${costo.name || 'SinNombre'}&valor=${costo.value}`)
        );

        // 2. Creamos las peticiones para agregar todos los costos indirectos
        const peticionesIndirectos = request.costosIndirectos.map(costo =>
          this.http.get(`${this.apiUrl}/agregar-indirecto?concepto=${costo.name || 'SinNombre'}&valor=${costo.value}`)
        );

        const todasLasPeticiones = [...peticionesDirectos, ...peticionesIndirectos];

        // Si no hay costos, devolvemos 0
        if (todasLasPeticiones.length === 0) {
          return of({ totalProyecto: 0 });
        }

        // 3. Ejecutamos todas las peticiones para agregar los costos
        return forkJoin(todasLasPeticiones).pipe(
          // 4. Finalmente, pedimos el total
          switchMap(() => this.http.get<any>(`${this.apiUrl}/total-proyecto`)),
          map(response => {
            return { totalProyecto: response.resultado };
          })
        );
      })
    );
  }
}
