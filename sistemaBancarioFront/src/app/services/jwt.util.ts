import { Rol } from '../models/usuario.model';

export interface JwtClaims {
  sub?: string;
  exp?: number;
  [claim: string]: unknown;
}

/** Decodifica el payload de un JWT sin validar la firma (eso es cosa del backend). */
export function decodeJwt(token: string): JwtClaims | null {
  const parts = token.split('.');
  if (parts.length !== 3) return null;
  try {
    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');
    const json = decodeURIComponent(
      atob(padded)
        .split('')
        .map((c) => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
        .join(''),
    );
    return JSON.parse(json) as JwtClaims;
  } catch {
    return null;
  }
}

/** `true` si el claim `exp` ya pasó (con 10s de margen). */
export function jwtExpirado(claims: JwtClaims): boolean {
  if (typeof claims.exp !== 'number') return false;
  return Date.now() >= claims.exp * 1000 - 10_000;
}

/** Lee el claim `rol` que el backend (`JwtService`) mete en el token: `USER` o `ADMIN`. */
export function rolDesdeClaims(claims: JwtClaims): Rol | null {
  switch (String(claims['rol'] ?? '').toUpperCase()) {
    case 'ADMIN':
      return Rol.ADMIN;
    case 'USER':
      return Rol.USER;
    default:
      return null;
  }
}
