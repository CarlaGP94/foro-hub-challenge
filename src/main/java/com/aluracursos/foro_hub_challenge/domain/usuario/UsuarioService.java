package com.aluracursos.foro_hub_challenge.domain.usuario;

import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioDetalle;
import com.aluracursos.foro_hub_challenge.infra.exception.UsuarioDuplicadoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository repository;

    @Transactional
    public UsuarioDetalle guardarNuevoUsuario(UsuarioCreacionDetalle datos){

        var usuarioDB = repository.findByEmail(datos.email());

        if(usuarioDB != null) {
            var usuario = new Usuario(datos);
            repository.save(usuario);
            return new UsuarioDetalle(usuario);
        }

        throw new UsuarioDuplicadoException("Usuario duplicado.");
    }

    public Page<UsuarioDetalle> listarUsuarios(Pageable paginacion){

        return repository.findAll(paginacion)
                .map(UsuarioDetalle::new);
    }

    public UsuarioDetalle listarUsuarioPorId(Long id){

        var usuario = repository.getReferenceById(id);

        return new UsuarioDetalle(usuario);
    }

    @Transactional
    public UsuarioDetalle actualizarUnUsuario(Long id, UsuarioActualizacionDetalle datos){

        // 1er filtro: buscar el id en DB
        Usuario usuarioExisteId = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado para actualizar.")); // 404

        // 2do filtro: Regla de negocio -> no pueden haber usuarios con el mismo email.
        // Solo busca duplicados si el email es diferente al ingresado.
        if (!usuarioExisteId.getEmail().equals(datos.email())) {

            // Busca que no coincida con otros mails de usuarios ya existentes.
            Optional<Usuario> usuarioDuplicado = repository.getReferenceByEmail(datos.email());

            // Si encontramos un usuario con el mismo email...
            if (usuarioDuplicado.isPresent()) {
                // ... Y ese usuario NO es el mismo que estamos actualizando (su ID es diferente).
                if (!usuarioDuplicado.get().getId().equals(id)) {
                    throw new UsuarioDuplicadoException("Email existente en otro usuario."); // 409
                }
            }
        }

        // Pasa todos los filtros
        usuarioExisteId.actualizarUsuario(datos);

        return new UsuarioDetalle(usuarioExisteId);
    }

    @Transactional
    public void eliminarUnUsuario(Long id){

        Usuario usuarioExisteId = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado.")); // 404

        repository.delete(usuarioExisteId);
    }
}
