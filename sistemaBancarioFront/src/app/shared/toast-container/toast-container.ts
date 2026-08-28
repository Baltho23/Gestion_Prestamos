import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-toast-container',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="pointer-events-none fixed inset-x-0 top-4 z-50 flex flex-col items-center gap-2 px-4">
      @for (n of notis.items(); track n.id) {
        <div
          class="pointer-events-auto flex w-full max-w-sm items-start gap-3 rounded-lg border px-4 py-3 text-sm shadow-lg"
          [class]="estilo(n.tipo)"
          role="status"
        >
          <span class="flex-1">{{ n.mensaje }}</span>
          <button
            type="button"
            class="shrink-0 opacity-60 transition hover:opacity-100"
            (click)="notis.cerrar(n.id)"
            aria-label="Cerrar"
          >
            ✕
          </button>
        </div>
      }
    </div>
  `,
})
export class ToastContainer {
  protected readonly notis = inject(NotificationService);

  protected estilo(tipo: string): string {
    switch (tipo) {
      case 'exito':
        return 'border-emerald-200 bg-emerald-50 text-emerald-800';
      case 'error':
        return 'border-red-200 bg-red-50 text-red-800';
      default:
        return 'border-slate-200 bg-white text-slate-800';
    }
  }
}
