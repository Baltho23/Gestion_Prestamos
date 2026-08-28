import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Prestamo } from '../../models/prestamo.model';
import { PrestamoService } from '../../services/prestamo.service';
import { NotificationService } from '../../services/notification.service';
import { AppHeader } from '../../shared/app-header/app-header';
import { claseBadgeEstado } from '../../shared/estado-prestamo.ui';

@Component({
  selector: 'app-prestamos-usuario',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, CurrencyPipe, AppHeader],
  templateUrl: './prestamos-usuario.html',
})
export class PrestamosUsuario {
  private readonly fb = inject(FormBuilder);
  private readonly prestamos = inject(PrestamoService);
  private readonly notis = inject(NotificationService);

  protected readonly badge = claseBadgeEstado;

  protected readonly lista = signal<Prestamo[]>([]);
  protected readonly cargando = signal(true);
  protected readonly enviando = signal(false);

  protected readonly form = this.fb.group({
    monto: this.fb.control<number | null>(null, [Validators.required, Validators.min(1)]),
    plazoMeses: this.fb.control<number | null>(null, [Validators.required, Validators.min(1)]),
  });

  constructor() {
    this.refrescar();
  }

  protected solicitar(): void {
    if (this.form.invalid || this.enviando()) {
      this.form.markAllAsTouched();
      return;
    }

    const { monto, plazoMeses } = this.form.getRawValue();
    this.enviando.set(true);
    this.prestamos.solicitar({ monto: monto!, plazoMeses: plazoMeses! }).subscribe({
      next: () => {
        this.enviando.set(false);
        this.form.reset({ monto: null, plazoMeses: null });
        this.notis.exito('Solicitud enviada.');
        this.refrescar();
      },
      error: (err) => {
        this.enviando.set(false);
        this.notis.error(err?.error?.mensaje ?? 'No se pudo enviar la solicitud.');
      },
    });
  }

  private refrescar(): void {
    this.cargando.set(true);
    this.prestamos.misPrestamos().subscribe({
      next: (data) => {
        this.lista.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.notis.error('No se pudieron cargar tus préstamos.');
      },
    });
  }
}
