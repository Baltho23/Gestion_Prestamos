package com.makers.sistemabancario.dominio.modelo;

import java.time.LocalDateTime;

public class Usuario {

    private final Long id;
    private final String email;
    private final String password;
    private final String nombre;
    private final Rol rol;
    private final LocalDateTime fechaCreacion;

    public Usuario(Long id, String email, String password, String nombre, Rol rol, LocalDateTime fechaCreacion) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
    }

    public boolean esAdmin() {
        return Rol.ADMIN.equals(this.rol);
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
