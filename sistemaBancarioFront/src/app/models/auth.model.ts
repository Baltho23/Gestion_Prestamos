import { Rol } from './usuario.model';

export interface LoginRequest {
  email: string;
  password: string;
}

/** El backend solo devuelve el token; el resto de la sesión sale de decodificarlo. */
export interface LoginResponse {
  token: string;
}

/** Usuario de la sesión, reconstruido a partir de los claims del JWT (`sub`, `rol`, `nombre`). */
export interface SesionUsuario {
  email: string;
  nombre: string;
  rol: Rol;
}
