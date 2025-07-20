import { Component, OnInit } from '@angular/core';
import { InstrumentoService } from '../../core/services/instrumento.service';
import { VentaService } from '../../core/services/venta.service';
import { PromocionService } from '../../core/services/promocion.service';
import { InventarioService } from '../../core/services/inventario.service';
import { MonitorService } from '../../core/services/monitor.service';
import { Instrumento, EstadisticasPromocion, MonitorStats } from '../../core/models/instrumento.model';

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
  estadisticasPromocion: EstadisticasPromocion | null = null;
  monitorStats: MonitorStats | null = null;
  loading = true;
  sistemasActivos = {
    principal: false,
    kafka: false,
    rabbit: false
  };

  constructor(
    private instrumentoService: InstrumentoService,
    private ventaService: VentaService,
    private promocionService: PromocionService,
    private inventarioService: InventarioService,
    private monitorService: MonitorService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
    this.verificarSistemas();
  }

  cargarDatos(): void {
    this.instrumentoService.getAll().subscribe(
      instrumentos => {
        this.totalInstrumentos = instrumentos.length;
        this.totalStock = instrumentos.reduce((sum, inst) => sum + inst.stock, 0);
        this.valorInventario = instrumentos.reduce((sum, inst) => sum + (inst.precio * inst.stock), 0);
        this.instrumentosMasRecientes = instrumentos.slice(-5).reverse();
        this.loading = false;
      },
      error => {
        console.error('Error al cargar instrumentos:', error);
        this.loading = false;
      }
    );

    this.promocionService.obtenerEstadisticas().subscribe(
      stats => {
        this.estadisticasPromocion = stats;
      },
      error => {
        console.error('Error al cargar estadísticas de promociones:', error);
      }
    );

    this.monitorService.obtenerEstadisticas().subscribe(
      stats => {
        this.monitorStats = stats;
      },
      error => {
        console.error('Error al cargar estadísticas del monitor:', error);
      }
    );
  }

  verificarSistemas(): void {
    this.instrumentoService.getAll().subscribe(
      () => { this.sistemasActivos.principal = true; },
      () => { this.sistemasActivos.principal = false; }
    );

    this.ventaService.healthCheckVentas().subscribe(
      () => { this.sistemasActivos.kafka = true; },
      () => { this.sistemasActivos.kafka = false; }
    );

    this.monitorService.healthCheck().subscribe(
      () => { this.sistemasActivos.rabbit = true; },
      () => { this.sistemasActivos.rabbit = false; }
    );
  }
}