package com.aluracursos.foro_hub_challenge.domain.topico;

import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoDetalle;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity(name = "Topico")
@Table(name = "topicos")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String mensaje;
    private LocalDateTime fechaDeCreacion;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String autor;
    @Enumerated(EnumType.STRING)
    private Curso curso;

    public Topico(TopicoDetalle datos) {
        this(
                datos.id(),
                datos.titulo(),
                datos.mensaje(),
                datos.fechaDeCreacion(),
                datos.status(),
                datos.autor(),
                datos.curso()
        );
    }

    public Topico(TopicoCreacionDetalle datos){
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.autor = datos.autor();
        this.curso = datos.curso();
        this.fechaDeCreacion = LocalDateTime.now();
        this.status = Status.ABIERTO;
    }

    public void actualizarTopico(TopicoActualizacionDetalle datos){
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.autor = datos.autor();
        this.curso = datos.curso();
    }
}
