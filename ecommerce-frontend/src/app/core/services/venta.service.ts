import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Venta } from '../models/instrumento.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private k1BaseUrl = 'http://localhost:8091';
  private r1BaseUrl = 'http://localhost:8081';

  constructor(private apiService: ApiService) { }

  procesarVenta(venta: Venta): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.k1BaseUrl}/api/ventas`, venta);
  }

  procesarVentaRabbit(venta: Venta): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.r1BaseUrl}/api/ventas/procesar`, venta);
  }

  listarVentas(): Observable<Venta[]> {
    return this.apiService.getWithCustomUrl<Venta[]>(`${this.k1BaseUrl}/api/ventas`);
  }

  procesarVentasBatch(ventas: Venta[]): Observable<any> {
    return this.apiService.postWithCustomUrl(`${this.k1BaseUrl}/api/ventas/batch`, ventas);
  }

  healthCheckVentas(): Observable<any> {
    return this.apiService.getWithCustomUrl(`${this.k1BaseUrl}/api/ventas/health`);
  }

  healthCheckVentasRabbit(): Observable<any> {
    return this.apiService.getWithCustomUrl(`${this.r1BaseUrl}/api/ventas/health`);
  }
}