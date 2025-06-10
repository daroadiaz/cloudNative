import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { InstrumentosListComponent } from './features/instrumentos-list/instrumentos-list.component';
import { InstrumentoFormComponent } from './features/instrumento-form/instrumento-form.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'instrumentos', component: InstrumentosListComponent, canActivate: [AuthGuard] },
  { path: 'instrumento/nuevo', component: InstrumentoFormComponent, canActivate: [AuthGuard] },
  { path: 'instrumento/editar/:id', component: InstrumentoFormComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }