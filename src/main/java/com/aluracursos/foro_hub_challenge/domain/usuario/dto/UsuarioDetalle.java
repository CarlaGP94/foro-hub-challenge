package com.aluracursos.foro_hub_challenge.domain.usuario.dto;

import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;

public record UsuarioDetalle(
        Long id,
        String nombre,
        String email
){
    public UsuarioDetalle(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    public Long getId() {
        return id;
    }
}
