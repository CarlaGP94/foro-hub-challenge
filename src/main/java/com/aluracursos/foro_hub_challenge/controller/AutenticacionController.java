package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.usuario.AutenticacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;
import com.aluracursos.foro_hub_challenge.infra.security.TokenDetalle;
import com.aluracursos.foro_hub_challenge.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager; // cumple el trabajo del service: metodo loadUserByUsername

    @PostMapping
    public ResponseEntity iniciarSesion (@RequestBody @Valid AutenticacionDetalle datos){

        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.nombre(),datos.contrasena()); // conversión DTO a Authentication
        var autenticacion = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal()); // token tipo "Usuario" - entidad JAVA

        return ResponseEntity.ok(new TokenDetalle(tokenJWT)); // token tipo DTO
    }
}
