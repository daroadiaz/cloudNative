import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { InventarioItem } from '../models/instrumento.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class InventarioService {
  private k2BaseUrl = 'http://localhost:8092';

  constructor(private apiService: ApiService) { }

  listarInventario(): Observable<InventarioItem[]> {
    return this.apiService.getWithCustomUrl<InventarioItem[]>(`${this.k2BaseUrl}/api/inventario`);
  }

  agregarProducto(producto: InventarioItem): Observable<InventarioItem> {
    return this.apiService.postWithCustomUrl<InventarioItem>(`${this.k2BaseUrl}/api/inventario/productos`, producto);
  }

  obtenerProductosBajoStock(): Observable<InventarioItem[]> {
    return this.apiService.getWithCustomUrl<InventarioItem[]>(`${this.k2BaseUrl}/api/inventario/bajo-stock`);
  }

  reabastecerProducto(id: number, cantidad: number): Observable<any> {
    const params = new HttpParams().set('cantidad', cantidad.toString());
    return this.apiService.postWithCustomUrl(`${this.k2BaseUrl}/api/inventario/restock/${id}?cantidad=${cantidad}`, {});
  }
}