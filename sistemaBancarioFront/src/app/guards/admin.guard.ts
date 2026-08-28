import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

/**
 * Solo ADMIN. Un USER autenticado se redirige a su propia vista; alguien sin
 * sesión, al login. El backend también lo aplica (@PreAuthorize) — esto es UX.
 */
export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.estaAutenticado() && auth.esAdmin()) return true;

  return router.createUrlTree([auth.estaAutenticado() ? '/prestamos' : '/login']);
};
