package com.aluracursos.foro_hub_challenge.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioActualizacionDetalle(
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "\\d{6}") String contrasena
) {
}
