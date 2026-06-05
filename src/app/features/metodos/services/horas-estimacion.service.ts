import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface HorasResponse {
  totalHoras: number;
  tarifa: number;
  tareas: number;
  promedio: number;
  costoTotal: number;
}

@Injectable({ providedIn: 'root' })
export class HorasEstimacionService {
  private readonly url = 'http://localhost:8082/api/v1/horas/calcular';

  constructor(private readonly http: HttpClient) {}

  calcular(tareas: number, promedio: number, tarifa: number): Observable<HorasResponse> {
    let params = new HttpParams()
      .set('tareas', String(tareas))
      .set('promedio', String(promedio))
      .set('tarifa', String(tarifa));

    return this.http.get<HorasResponse>(this.url, { params });
  }
}
