import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse, User } from '../models/instrumento.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(
        map(response => {
          if (response && response.token) {
            const user: User = {
              username: response.user || credentials.username,
              token: response.token
            };
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('token', response.token);
            this.currentUserSubject.next(user);
            return {
              success: true,
              token: response.token,
              user: response.user || credentials.username,
              message: 'Login exitoso'
            };
          }
          return {
            success: false,
            token: '',
            user: '',
            message: 'Error en el login'
          };
        }),
        catchError(error => {
          console.error('Error en login:', error);
          if (error.status === 200 && error.error && error.error.text) {
            try {
              const response = JSON.parse(error.error.text);
              if (response.token) {
                const user: User = {
                  username: response.user || credentials.username,
                  token: response.token
                };
                localStorage.setItem('currentUser', JSON.stringify(user));
                localStorage.setItem('token', response.token);
                this.currentUserSubject.next(user);
                return of({
                  success: true,
                  token: response.token,
                  user: response.user || credentials.username,
                  message: 'Login exitoso'
                });
              }
            } catch (e) {
              console.error('Error parseando respuesta:', e);
            }
          }
          return of({
            success: false,
            token: '',
            user: '',
            message: 'Credenciales inválidas'
          });
        })
      );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    const user = this.currentUserValue;
    return !!(token && user);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  verifyToken(): Observable<boolean> {
    return this.http.get<any>(`${environment.apiUrl}/auth/verify`)
      .pipe(
        map(() => true),
        catchError(() => of(false))
      );
  }
}