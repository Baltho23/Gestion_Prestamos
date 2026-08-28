import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

import { EstadoPrestamo, Prestamo } from '../../models/prestamo.model';
import { PrestamoService } from '../../services/prestamo.service';
import { NotificationService } from '../../services/notification.service';
import { AppHeader } from '../../shared/app-header/app-header';
import { claseBadgeEstado } from '../../shared/estado-prestamo.ui';

@Component({
  selector: 'app-prestamos-admin',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CurrencyPipe, AppHeader],
  templateUrl: './prestamos-admin.html',
})
export class PrestamosAdmin {
  private readonly prestamos = inject(PrestamoService);
  private readonly notis = inject(NotificationService);

  protected readonly badge = claseBadgeEstado;
  protected readonly Estado = EstadoPrestamo;

  protected readonly lista = signal<Prestamo[]>([]);
  protected readonly cargando = signal(true);
  /** Id de la fila cuya acción está en curso (deshabilita solo esa fila). */
  protected readonly procesando = signal<number | null>(null);

  protected readonly pendientes = computed(
    () => this.lista().filter((p) => p.estado === EstadoPrestamo.PENDIENTE).length,
  );

  constructor() {
    this.refrescar();
  }

  protected aprobar(p: Prestamo): void {
    this.resolver(p, 'aprobar');
  }

  protected rechazar(p: Prestamo): void {
    this.resolver(p, 'rechazar');
  }

  private resolver(p: Prestamo, accion: 'aprobar' | 'rechazar'): void {
    if (this.procesando() !== null) return;

    this.procesando.set(p.id);
    this.prestamos[accion](p.id).subscribe({
      next: () => {
        this.procesando.set(null);
        this.notis.exito(`Préstamo #${p.id} ${accion === 'aprobar' ? 'aprobado' : 'rechazado'}.`);
        this.refrescar();
      },
      error: (err) => {
        this.procesando.set(null);
        if (err?.status === 409) {
          this.notis.error('Este préstamo ya fue resuelto por otro administrador.');
          this.refrescar();
        } else {
          this.notis.error(err?.error?.mensaje ?? 'No se pudo completar la acción.');
        }
      },
    });
  }

  private refrescar(): void {
    this.cargando.set(true);
    this.prestamos.listarTodos().subscribe({
      next: (data) => {
        this.lista.set(
          [...data].sort((a, b) => {
            const pend = Number(b.estado === EstadoPrestamo.PENDIENTE) - Number(a.estado === EstadoPrestamo.PENDIENTE);
            return pend !== 0 ? pend : b.id - a.id;
          }),
        );
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.notis.error('No se pudieron cargar las solicitudes.');
      },
    });
  }
}
