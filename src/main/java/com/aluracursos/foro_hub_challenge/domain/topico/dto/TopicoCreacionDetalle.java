package com.aluracursos.foro_hub_challenge.domain.topico.dto;

import com.aluracursos.foro_hub_challenge.domain.topico.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoCreacionDetalle(
        @NotBlank(message = "El campo 'título' es obligatorio.") // Mensaje de error personalizado
        String titulo,
        @NotBlank(message = "El campo 'mensaje' es obligatorio.")
        String mensaje,
        @NotBlank(message = "El campo 'autor' es obligatorio.")
        String autor,
        @NotNull(message = "El campo 'curso' es obligatorio.")
        Curso curso
) {

}
