import { EstadoPrestamo } from '../models/prestamo.model';

/** Clases Tailwind para el badge de estado, compartidas entre las vistas user y admin. */
export function claseBadgeEstado(estado: EstadoPrestamo): string {
  switch (estado) {
    case EstadoPrestamo.APROBADO:
      return 'bg-emerald-100 text-emerald-800';
    case EstadoPrestamo.RECHAZADO:
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-amber-100 text-amber-800';
  }
}
