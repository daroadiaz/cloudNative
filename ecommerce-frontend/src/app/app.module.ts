import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { InstrumentosListComponent } from './features/instrumentos-list/instrumentos-list.component';
import { InstrumentoFormComponent } from './features/instrumento-form/instrumento-form.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';

import { ApiService } from './core/services/api.service';
import { AuthService } from './core/services/auth.service';
import { InstrumentoService } from './core/services/instrumento.service';
import { VentaService } from './core/services/venta.service';
import { PromocionService } from './core/services/promocion.service';
import { InventarioService } from './core/services/inventario.service';
import { MonitorService } from './core/services/monitor.service';

import { AuthGuard } from './core/guards/auth.guard';

import { AuthInterceptor } from './core/interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    InstrumentosListComponent,
    InstrumentoFormComponent,
    NavbarComponent,
    LoadingSpinnerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    ApiService,
    AuthService,
    InstrumentoService,
    VentaService,
    PromocionService,
    InventarioService,
    MonitorService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }