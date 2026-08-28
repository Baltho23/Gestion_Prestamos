import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [AsyncPipe],
  template: `
    <header class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-4xl items-center justify-between px-4 py-3">
        <div class="flex items-center gap-2">
          <span class="text-sm font-semibold text-slate-900">Sistema de Préstamos</span>
          @if (auth.usuarioActual$ | async; as u) {
            <span
              class="rounded-full bg-slate-100 px-2 py-0.5 text-xs font-medium text-slate-600"
            >
              {{ u.rol }}
            </span>
          }
        </div>
        <div class="flex items-center gap-3 text-sm">
          @if (auth.usuarioActual$ | async; as u) {
            <span class="text-slate-700">Hola, {{ u.nombre }}</span>
          }
          <button
            type="button"
            class="rounded-lg border border-slate-300 px-3 py-1.5 font-medium text-slate-700 transition hover:bg-slate-50"
            (click)="salir()"
          >
            Salir
          </button>
        </div>
      </div>
    </header>
  `,
})
export class AppHeader {
  protected readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected salir(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
