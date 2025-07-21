package com.aluracursos.foro_hub_challenge.domain.topico.dto;

import com.aluracursos.foro_hub_challenge.domain.topico.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicoActualizacionDetalle(
        @NotBlank(message = "El campo 'título' es obligatorio si quiere actualizar el tópico.")
        String titulo,
        @NotBlank(message = "El campo 'mensaje' es obligatorio si quiere actualizar el tópico.")
        String mensaje,
        @NotNull(message = "El campo 'curso' es obligatorio si quiere actualizar el tópico.")
        Curso curso
) {
}
