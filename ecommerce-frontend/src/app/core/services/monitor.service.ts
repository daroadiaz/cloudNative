import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MonitorStats } from '../models/instrumento.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class MonitorService {
  private r2BaseUrl = 'http://localhost:8082';

  constructor(private apiService: ApiService) { }

  healthCheck(): Observable<any> {
    return this.apiService.getWithCustomUrl(`${this.r2BaseUrl}/api/monitor/health`);
  }

  listarArchivos(): Observable<string[]> {
    return this.apiService.getWithCustomUrl<string[]>(`${this.r2BaseUrl}/api/monitor/files`);
  }

  obtenerEstadisticas(): Observable<MonitorStats> {
    return this.apiService.getWithCustomUrl<MonitorStats>(`${this.r2BaseUrl}/api/monitor/stats`);
  }
}