package com.makers.sistemabancario.infraestructura.web.controller;

import com.makers.sistemabancario.aplicacion.puerto.salida.UsuarioRepositorioPuerto;
import com.makers.sistemabancario.dominio.excepcion.UsuarioNoEncontradoException;
import com.makers.sistemabancario.dominio.modelo.Usuario;
import com.makers.sistemabancario.infraestructura.seguridad.JwtService;
import com.makers.sistemabancario.infraestructura.web.dto.request.LoginRequest;
import com.makers.sistemabancario.infraestructura.web.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepositorioPuerto usuarioRepositorioPuerto;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        Usuario usuario = usuarioRepositorioPuerto.buscarPorEmail(request.email())
                .orElseThrow(() -> new UsuarioNoEncontradoException(request.email()));
        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol().name(), usuario.getNombre());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
