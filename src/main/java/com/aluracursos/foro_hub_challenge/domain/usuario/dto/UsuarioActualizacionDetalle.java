package com.aluracursos.foro_hub_challenge.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UsuarioActualizacionDetalle(
        @Email String email,
        @Pattern(regexp = "\\d{6}") String contrasena
) {
}
