package com.aluracursos.foro_hub_challenge.domain.topico.dto;

import com.aluracursos.foro_hub_challenge.domain.topico.Curso;
import com.aluracursos.foro_hub_challenge.domain.topico.Status;
import com.aluracursos.foro_hub_challenge.domain.topico.Topico;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TopicoDetalle (
    Long id,
    String titulo,
    String mensaje,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    LocalDateTime fechaDeCreacion,
    Status status,
    String autor,
    Curso curso
) {
    public TopicoDetalle(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso()
        );
    }

    public Long getId() {
        return id;
    }
}
