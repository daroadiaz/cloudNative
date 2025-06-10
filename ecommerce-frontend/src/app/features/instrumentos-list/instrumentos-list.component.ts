import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InstrumentoService } from '../../core/services/instrumento.service';
import { Instrumento } from '../../core/models/instrumento.model';

@Component({
  selector: 'app-instrumentos-list',
  templateUrl: './instrumentos-list.component.html',
  styleUrls: ['./instrumentos-list.component.css']
})
export class InstrumentosListComponent implements OnInit {
  instrumentos: Instrumento[] = [];
  instrumentosFiltrados: Instrumento[] = [];
  loading = true;
  searchTerm = '';
  mensaje = '';
  tipoMensaje = '';

  constructor(
    private instrumentoService: InstrumentoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarInstrumentos();
  }

  cargarInstrumentos(): void {
    this.loading = true;
    this.instrumentoService.getAll().subscribe(
      data => {
        this.instrumentos = data;
        this.instrumentosFiltrados = data;
        this.loading = false;
      },
      error => {
        console.error('Error al cargar instrumentos:', error);
        this.mostrarMensaje('Error al cargar los instrumentos', 'danger');
        this.loading = false;
      }
    );
  }

  buscar(): void {
    if (this.searchTerm.trim() === '') {
      this.instrumentosFiltrados = this.instrumentos;
    } else {
      const termino = this.searchTerm.toLowerCase();
      this.instrumentosFiltrados = this.instrumentos.filter(inst =>
        inst.nombre.toLowerCase().includes(termino) ||
        inst.marca.toLowerCase().includes(termino) ||
        inst.tipo.toLowerCase().includes(termino)
      );
    }
  }

  editarInstrumento(id: number | undefined): void {
    if (id) {
      this.router.navigate(['/instrumento/editar', id]);
    }
  }

  eliminarInstrumento(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('¿Está seguro de eliminar este instrumento?')) {
      this.instrumentoService.delete(id).subscribe(
        () => {
          this.mostrarMensaje('Instrumento eliminado exitosamente', 'success');
          this.cargarInstrumentos();
        },
        error => {
          console.error('Error al eliminar:', error);
          this.mostrarMensaje('Error al eliminar el instrumento', 'danger');
        }
      );
    }
  }

  mostrarMensaje(mensaje: string, tipo: string): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
    setTimeout(() => {
      this.mensaje = '';
      this.tipoMensaje = '';
    }, 5000);
  }
}