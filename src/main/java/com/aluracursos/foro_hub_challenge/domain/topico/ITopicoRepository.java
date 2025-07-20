package com.aluracursos.foro_hub_challenge.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ITopicoRepository extends JpaRepository<Topico,Long> {

    // Metodo para buscar un tópico por título y mensaje.
    // Retorna un Optional<Topico> para manejar la ausencia de un resultado.
    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);

    Page<Topico> findAll(Pageable paginacion);

}
