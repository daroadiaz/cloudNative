import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Instrumento } from '../models/instrumento.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class InstrumentoService {

  constructor(private apiService: ApiService) { }

  getAll(): Observable<Instrumento[]> {
    return this.apiService.get<Instrumento[]>('/instrumentos');
  }

  getById(id: number): Observable<Instrumento> {
    return this.apiService.get<Instrumento>(`/instrumentos/${id}`);
  }

  create(instrumento: Instrumento): Observable<Instrumento> {
    return this.apiService.post<Instrumento>('/instrumentos', instrumento);
  }

  update(id: number, instrumento: Instrumento): Observable<Instrumento> {
    return this.apiService.put<Instrumento>(`/instrumentos/${id}`, instrumento);
  }

  delete(id: number): Observable<void> {
    return this.apiService.delete<void>(`/instrumentos/${id}`);
  }
}