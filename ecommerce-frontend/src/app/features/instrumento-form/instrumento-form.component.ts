import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InstrumentoService } from '../../core/services/instrumento.service';
import { Instrumento } from '../../core/models/instrumento.model';

@Component({
  selector: 'app-instrumento-form',
  templateUrl: './instrumento-form.component.html',
  styleUrls: ['./instrumento-form.component.css']
})
export class InstrumentoFormComponent implements OnInit {
  instrumentoForm: FormGroup;
  loading = false;
  isEditMode = false;
  instrumentoId: number | null = null;
  titulo = 'Nuevo Instrumento';

  tiposInstrumento = [
    'Guitarra Eléctrica',
    'Guitarra Acústica',
    'Guitarra Clásica',
    'Bajo Eléctrico',
    'Bajo Acústico',
    'Batería Acústica',
    'Batería Electrónica',
    'Piano Digital',
    'Teclado',
    'Saxofón',
    'Trompeta',
    'Violín',
    'Ukulele',
    'Otros'
  ];

  constructor(
    private formBuilder: FormBuilder,
    private instrumentoService: InstrumentoService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.instrumentoForm = this.formBuilder.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      marca: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      tipo: ['', Validators.required],
      precio: ['', [Validators.required, Validators.min(0.01), Validators.max(999999)]],
      stock: ['', [Validators.required, Validators.min(0), Validators.max(9999)]]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params.id) {
        this.instrumentoId = +params.id;
        this.isEditMode = true;
        this.titulo = 'Editar Instrumento';
        this.cargarInstrumento();
      }
    });
  }

  get f() { return this.instrumentoForm.controls; }

  cargarInstrumento(): void {
    if (this.instrumentoId) {
      this.loading = true;
      this.instrumentoService.getById(this.instrumentoId).subscribe(
        instrumento => {
          this.instrumentoForm.patchValue(instrumento);
          this.loading = false;
        },
        error => {
          console.error('Error al cargar instrumento:', error);
          this.router.navigate(['/instrumentos']);
        }
      );
    }
  }

  onSubmit(): void {
    if (this.instrumentoForm.invalid) {
      Object.keys(this.instrumentoForm.controls).forEach(key => {
        this.instrumentoForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    const instrumento: Instrumento = this.instrumentoForm.value;

    if (this.isEditMode && this.instrumentoId) {
      this.instrumentoService.update(this.instrumentoId, instrumento).subscribe(
        () => {
          this.router.navigate(['/instrumentos']);
        },
        error => {
          console.error('Error al actualizar:', error);
          this.loading = false;
        }
      );
    } else {
      this.instrumentoService.create(instrumento).subscribe(
        () => {
          this.router.navigate(['/instrumentos']);
        },
        error => {
          console.error('Error al crear:', error);
          this.loading = false;
        }
      );
    }
  }

  cancelar(): void {
    this.router.navigate(['/instrumentos']);
  }
}