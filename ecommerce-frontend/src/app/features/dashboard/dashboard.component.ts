import { Component, OnInit } from '@angular/core';
import { InstrumentoService } from '../../core/services/instrumento.service';
import { Instrumento } from '../../core/models/instrumento.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  totalInstrumentos = 0;
  totalStock = 0;
  valorInventario = 0;
  instrumentosMasRecientes: Instrumento[] = [];
  loading = true;

  constructor(private instrumentoService: InstrumentoService) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.instrumentoService.getAll().subscribe(
      instrumentos => {
        this.totalInstrumentos = instrumentos.length;
        this.totalStock = instrumentos.reduce((sum, inst) => sum + inst.stock, 0);
        this.valorInventario = instrumentos.reduce((sum, inst) => sum + (inst.precio * inst.stock), 0);
        
        // Obtener los 5 instrumentos más recientes
        this.instrumentosMasRecientes = instrumentos.slice(-5).reverse();
        
        this.loading = false;
      },
      error => {
        console.error('Error al cargar instrumentos:', error);
        this.loading = false;
      }
    );
  }
}