package com.aluracursos.foro_hub_challenge.domain.topico;

import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;
    @Enumerated(EnumType.STRING)
    private Curso curso;

//    public Topico(TopicoDetalle datos) {
//        this(
//                datos.id(),
//                datos.titulo(),
//                datos.mensaje(),
//                datos.fechaDeCreacion(),
//                datos.status(),
//                null,
//                datos.curso()
//        );
//    }
    // Constructor para crear un nuevo tópico.
    public Topico(String titulo, String mensaje, Usuario autor, Curso curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.autor = autor;
        this.curso = curso;
        this.fechaDeCreacion = LocalDateTime.now();
        this.status = Status.ABIERTO;
    }

    public void actualizarTopico(TopicoActualizacionDetalle datos){
        if (datos.titulo() != null && !datos.titulo().isBlank()) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null && !datos.mensaje().isBlank()) {
            this.mensaje = datos.mensaje();
        }
        if (datos.curso() != null) {
            this.curso = datos.curso();
        }
    }

}
