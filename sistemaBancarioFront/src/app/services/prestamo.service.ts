import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Prestamo } from '../models/prestamo.model';
import { SolicitarPrestamoRequest } from '../models/prestamo-request.model';

@Injectable({ providedIn: 'root' })
export class PrestamoService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/prestamos`;

  /** USER: crea una solicitud de préstamo. */
  solicitar(datos: SolicitarPrestamoRequest): Observable<Prestamo> {
    return this.http.post<Prestamo>(this.base, datos);
  }

  /** USER: préstamos del usuario autenticado. */
  misPrestamos(): Observable<Prestamo[]> {
    return this.http.get<Prestamo[]>(`${this.base}/mis`);
  }

  /** ADMIN: todas las solicitudes. */
  listarTodos(): Observable<Prestamo[]> {
    return this.http.get<Prestamo[]>(this.base);
  }

  /** ADMIN: aprueba una solicitud pendiente (PATCH sin body). */
  aprobar(id: number): Observable<Prestamo> {
    return this.http.patch<Prestamo>(`${this.base}/${id}/aprobar`, {});
  }

  /** ADMIN: rechaza una solicitud pendiente (PATCH sin body). */
  rechazar(id: number): Observable<Prestamo> {
    return this.http.patch<Prestamo>(`${this.base}/${id}/rechazar`, {});
  }
}
