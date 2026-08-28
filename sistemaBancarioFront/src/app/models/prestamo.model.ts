export enum EstadoPrestamo {
  PENDIENTE = 'PENDIENTE',
  APROBADO = 'APROBADO',
  RECHAZADO = 'RECHAZADO',
}

/**
 * Espejo de `PrestamoResponse` del backend.
 *
 * `monto` se tipa como `number`: Jackson serializa `BigDecimal` como número JSON
 * y el backend no declara un formato custom. La precisión decimal vive en el
 * backend (NUMERIC(15,2)); el front solo lo muestra formateado.
 *
 * El backend incluye `solicitanteNombre` para que la vista admin identifique
 * quién pidió el préstamo sin exponer el correo.
 */
export interface Prestamo {
  id: number;
  usuarioId: number;
  solicitanteNombre: string | null;
  resueltoPorId: number | null;
  /** Nombre del admin que aprobó/rechazó; `null` mientras está PENDIENTE. */
  resueltoPorNombre: string | null;
  monto: number;
  plazoMeses: number;
  estado: EstadoPrestamo;
  fechaSolicitud: string;
  fechaResolucion: string | null;
}
