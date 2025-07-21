package com.aluracursos.foro_hub_challenge.domain.topico;

import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.IUsuarioRepository;
import com.aluracursos.foro_hub_challenge.domain.usuario.Usuario;
import com.aluracursos.foro_hub_challenge.infra.exception.TopicoDuplicadoException;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoDetalle;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private ITopicoRepository topicoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Transactional
    public TopicoDetalle guardarNuevoTopico(TopicoCreacionDetalle datos){

        // Regla de negocio: busca si el título y el mensaje ya existen en la DB.
        topicoRepository.findByTituloAndMensaje(datos.titulo(), datos.mensaje())
                .ifPresent(topicoExistente -> {
                    throw new TopicoDuplicadoException("Ya existe un tópico con el mismo título y mensaje.");
                });

        // Buscar la entidad Usuario por su ID (el autor del tópico), si no está -> 404
        Usuario autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("El autor con ID " + datos.autorId() + " no fue encontrado."));


        var topico = new Topico(datos.titulo(), datos.mensaje(), autor, datos.curso());

        // Guarda el nuevo tópico en la base de datos.
        topico = topicoRepository.save(topico);

        return new TopicoDetalle(topico);
    }

    public Page<TopicoDetalle> listarTopicos(Pageable paginacion){

        return topicoRepository.findAll(paginacion)
                .map(TopicoDetalle::new);
    }

    public TopicoDetalle listarTopicoPorId(Long id){

        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico con ID " + id + " no encontrado."));

        return new TopicoDetalle(topico);
    }

    @Transactional
    public TopicoDetalle actualizarUnTopico(Long id, TopicoActualizacionDetalle datos){

        // 1er filtro: buscar el id en DB
        Topico topicoExisteId = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico con ID " + id + " no encontrado para actualizar.")); // 404

        // 2do filtro: Regla de negocio -> no pueden haber tópicos con el mismo título y mensaje.
        // Solo busca duplicados si los datos de título/mensaje son diferentes a los actuales del tópico.
        if (!topicoExisteId.getTitulo().equals(datos.titulo()) || !topicoExisteId.getMensaje().equals(datos.mensaje())) {

            // Busca que no coincida con otros ya existentes.
            Optional<Topico> topicoDuplicado = topicoRepository.findByTituloAndMensaje(datos.titulo(), datos.mensaje());

            // Si encontramos un tópico con el mismo título y mensaje...
            if (topicoDuplicado.isPresent()) {
                // ... Y ese tópico NO es el mismo que estamos actualizando (su ID es diferente).
                if (!topicoDuplicado.get().getId().equals(id)) {
                    throw new TopicoDuplicadoException("La combinación de título y mensaje ya existe en otro tópico."); // 409
                }
            }
        }

        // Pasa todos los filtros
        topicoExisteId.actualizarTopico(datos);

        return new TopicoDetalle(topicoExisteId);
    }

    @Transactional
    public void eliminarUnTopico(Long id){

        Topico topicoExisteId = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico con ID " + id + " no encontrado.")); // 404

        topicoRepository.delete(topicoExisteId);
    }
}
