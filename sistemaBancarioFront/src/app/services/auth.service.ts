import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';
import { Rol } from '../models/usuario.model';
import { LoginRequest, LoginResponse, SesionUsuario } from '../models/auth.model';
import { decodeJwt, jwtExpirado, rolDesdeClaims } from './jwt.util';

const TOKEN_KEY = 'sb_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/auth`;

  private readonly usuarioActualSubject = new BehaviorSubject<SesionUsuario | null>(
    this.sesionDesdeToken(this.getToken()),
  );

  /** Usuario de la sesión actual (o `null` si no hay sesión válida). */
  readonly usuarioActual$: Observable<SesionUsuario | null> =
    this.usuarioActualSubject.asObservable();

  login(datos: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.base}/login`, datos).pipe(
      tap((res) => this.guardarSesion(res.token)),
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.usuarioActualSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  estaAutenticado(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const claims = decodeJwt(token);
    return claims !== null && !jwtExpirado(claims);
  }

  esAdmin(): boolean {
    return this.usuarioActualSubject.value?.rol === Rol.ADMIN;
  }

  private guardarSesion(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.usuarioActualSubject.next(this.sesionDesdeToken(token));
  }

  private sesionDesdeToken(token: string | null): SesionUsuario | null {
    if (!token) return null;
    const claims = decodeJwt(token);
    if (!claims || jwtExpirado(claims)) return null;
    const rol = rolDesdeClaims(claims);
    if (!rol) return null;
    const email = claims.sub ?? '';
    const nombre = (claims['nombre'] as string | undefined) ?? email;
    return { email, nombre, rol };
  }
}
