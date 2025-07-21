package com.aluracursos.foro_hub_challenge.domain.topico.dto;

import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;

public record UsuarioDetalleParaTopico(
        Long id,
        String nombre,
        String email
) {

    public UsuarioDetalleParaTopico(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}
