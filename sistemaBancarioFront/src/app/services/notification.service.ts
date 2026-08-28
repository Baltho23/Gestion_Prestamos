import { Injectable, signal } from '@angular/core';

export type TipoNotificacion = 'exito' | 'error' | 'info';

export interface Notificacion {
  id: number;
  tipo: TipoNotificacion;
  mensaje: string;
}

/**
 * Cola de toasts efímeros. Es estado puramente de vista (no de dominio), por eso
 * usa `signal` en vez del `BehaviorSubject` de los servicios de negocio.
 * Cada toast se autodescarta a los 4s; también se puede cerrar a mano.
 */
@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly _items = signal<Notificacion[]>([]);
  readonly items = this._items.asReadonly();

  private contador = 0;

  exito(mensaje: string): void {
    this.push('exito', mensaje);
  }

  error(mensaje: string): void {
    this.push('error', mensaje);
  }

  info(mensaje: string): void {
    this.push('info', mensaje);
  }

  cerrar(id: number): void {
    this._items.update((items) => items.filter((n) => n.id !== id));
  }

  private push(tipo: TipoNotificacion, mensaje: string): void {
    const id = ++this.contador;
    this._items.update((items) => [...items, { id, tipo, mensaje }]);
    setTimeout(() => this.cerrar(id), 4000);
  }
}
