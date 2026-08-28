import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login').then((m) => m.Login),
  },
  {
    path: 'prestamos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/prestamos-usuario/prestamos-usuario').then((m) => m.PrestamosUsuario),
  },
  {
    path: 'admin/prestamos',
    canActivate: [authGuard, adminGuard],
    loadComponent: () =>
      import('./features/prestamos-admin/prestamos-admin').then((m) => m.PrestamosAdmin),
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
