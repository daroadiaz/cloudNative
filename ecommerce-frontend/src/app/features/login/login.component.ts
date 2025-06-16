import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

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
  useAzureAD = true; // Cambiar a false si no quieres usar Azure AD

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
    // Si ya está autenticado, redirigir al dashboard
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  get f() { return this.loginForm.controls; }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  // Login con Azure AD
  loginWithAzure(): void {
    if (this.useAzureAD) {
      this.loading = true;
      try {
        this.authService.loginWithAzure();
      } catch (error) {
        console.error('Error al iniciar login con Azure:', error);
        this.errorMessage = 'Error al conectar con Azure AD';
        this.loading = false;
      }
    } else {
      alert('Login con Azure AD está deshabilitado');
    }
  }

  // Login tradicional
  onSubmit(): void {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe(
      response => {
        this.loading = false;
        if (response.success) {
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage = response.message || 'Credenciales inválidas';
        }
      },
      error => {
        this.loading = false;
        console.error('Error en login:', error);
        this.errorMessage = 'Error de conexión. Por favor, intente más tarde.';
      }
    );
  }
}