import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MsalService, MsalBroadcastService } from '@azure/msal-angular';
import { InteractionStatus, RedirectRequest, AuthenticationResult } from '@azure/msal-browser';
import { filter, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { loginRequest } from '../../auth-config';
import { LoginRequest, LoginResponse, User } from '../models/instrumento.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnDestroy {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;
  private baseUrl = environment.apiUrl;
  private readonly _destroying$ = new Subject<void>();

  constructor(
    private http: HttpClient,
    private router: Router,
    private msalService: MsalService,
    private msalBroadcastService: MsalBroadcastService
  ) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();

    // Escuchar cambios en el estado de autenticación de Azure AD
    this.msalBroadcastService.inProgress$
      .pipe(
        filter((status: InteractionStatus) => status === InteractionStatus.None),
        takeUntil(this._destroying$)
      )
      .subscribe(() => {
        this.checkAndSetActiveAccount();
      });
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  // Login con Azure AD
  loginWithAzure(): void {
    const loginRequestConfig: RedirectRequest = {
      ...loginRequest,
      prompt: 'select_account'
    };

    this.msalService.loginRedirect(loginRequestConfig);
  }

  // Login tradicional (para desarrollo/testing)
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, credentials)
      .pipe(
        map(response => {
          if (response.success && response.token && response.user) {
            const user: User = {
              username: response.user,
              token: response.token
            };
            localStorage.setItem('currentUser', JSON.stringify(user));
            localStorage.setItem('token', response.token);
            this.currentUserSubject.next(user);
          }
          return response;
        }),
        catchError(error => {
          console.error('Error en login:', error);
          // Retornar un response de error
          return of({
            success: false,
            message: 'Error de conexión con el servidor'
          } as LoginResponse);
        })
      );
  }

  // Logout
  logout(): void {
    // Limpiar almacenamiento local
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);

    // Si hay una sesión activa de Azure AD, cerrarla
    const activeAccount = this.msalService.instance.getActiveAccount();
    if (activeAccount) {
      const logoutRequest = {
        account: activeAccount,
        postLogoutRedirectUri: window.location.origin
      };
      this.msalService.logoutRedirect(logoutRequest);
    } else {
      this.router.navigate(['/login']);
    }
  }

  // Verificar si está autenticado
  isAuthenticated(): boolean {
    // Verificar autenticación tradicional
    if (this.currentUserValue) {
      return true;
    }
    
    // Verificar autenticación con Azure AD
    return this.msalService.instance.getAllAccounts().length > 0;
  }

  // Obtener token
  getToken(): string | null {
    // Primero intentar obtener token tradicional
    const traditionalToken = localStorage.getItem('token');
    if (traditionalToken) {
      return traditionalToken;
    }

    // Si no, obtener token de Azure AD
    const activeAccount = this.msalService.instance.getActiveAccount();
    if (activeAccount) {
      // El token se manejará automáticamente por el interceptor de MSAL
      return 'azure-ad-token';
    }

    return null;
  }

  // Verificar y establecer cuenta activa de Azure AD
  private checkAndSetActiveAccount(): void {
    const activeAccount = this.msalService.instance.getActiveAccount();

    if (!activeAccount && this.msalService.instance.getAllAccounts().length > 0) {
      const accounts = this.msalService.instance.getAllAccounts();
      this.msalService.instance.setActiveAccount(accounts[0]);
      
      // Crear usuario desde la cuenta de Azure AD
      const azureUser: User = {
        username: accounts[0].username,
        token: 'azure-ad-token'
      };
      
      localStorage.setItem('currentUser', JSON.stringify(azureUser));
      this.currentUserSubject.next(azureUser);
    } else if (activeAccount) {
      const azureUser: User = {
        username: activeAccount.username,
        token: 'azure-ad-token'
      };
      
      localStorage.setItem('currentUser', JSON.stringify(azureUser));
      this.currentUserSubject.next(azureUser);
    }
  }

  ngOnDestroy(): void {
    this._destroying$.next(undefined);
    this._destroying$.complete();
  }
}