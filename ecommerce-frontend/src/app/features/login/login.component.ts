import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  showPassword = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  get f() { return this.loginForm.controls; }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const credentials = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };

    this.authService.login(credentials).subscribe(
      response => {
        console.log('Respuesta de login:', response);
        if (response && response.success) {
          console.log('Login exitoso, redirigiendo...');
          this.router.navigate(['/dashboard']).then(
            () => {
              console.log('Navegación exitosa');
              this.loading = false;
            },
            (error) => {
              console.error('Error en navegación:', error);
              this.loading = false;
            }
          );
        } else {
          this.errorMessage = response.message || 'Error en el login';
          this.loading = false;
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Error en login component:', error);
        this.errorMessage = 'Error de conexión. Por favor, intente más tarde.';
        this.loading = false;
      }
    );
  }
}