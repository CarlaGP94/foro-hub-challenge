package com.aluracursos.foro_hub_challenge.domain.usuario.dto;

import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;
import org.jspecify.annotations.Nullable;

public record UsuarioDetalle(
        Long id,
        String nombre,
        String email,
        String contrasena
){
    public UsuarioDetalle(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContrasena()
        );
    }

    public Long getId() {
        return id;
    }
}
