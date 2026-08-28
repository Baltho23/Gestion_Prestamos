/**
 * Payload de "Solicitar Préstamo" — `POST /api/prestamos`.
 * Ambos campos salen del Reactive Form como `number`.
 */
export interface SolicitarPrestamoRequest {
  monto: number;
  plazoMeses: number;
}
