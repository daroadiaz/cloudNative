import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Promocion, EstadisticasPromocion } from '../models/instrumento.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class PromocionService {
  private k3BaseUrl = 'http://localhost:8093';
  private r1BaseUrl = 'http://localhost:8081';

  constructor(private apiService: ApiService) { }

  generarPromociones(): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.k3BaseUrl}/api/promociones/generar`, {});
  }

  analizarCandidatos(): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.k3BaseUrl}/api/promociones/analizar`, {});
  }

  listarPromocionesActivas(): Observable<Promocion[]> {
    return this.apiService.getWithCustomUrl<Promocion[]>(`${this.k3BaseUrl}/api/promociones/activas`);
  }

  obtenerEstadisticas(): Observable<EstadisticasPromocion> {
    return this.apiService.getWithCustomUrl<EstadisticasPromocion>(`${this.k3BaseUrl}/api/promociones/estadisticas`);
  }

  actualizarPromocion(promocion: Promocion): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.r1BaseUrl}/api/promociones/actualizar`, promocion);
  }

  enviarPromocionesBatch(promociones: Promocion[]): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.r1BaseUrl}/api/promociones/batch`, promociones);
  }

  healthCheckPromociones(): Observable<any> {
    return this.apiService.getWithCustomUrl(`${this.r1BaseUrl}/api/promociones/health`);
  }
}