package com.aluracursos.foro_hub_challenge.domain.usuario;

import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioDetalle;
import com.aluracursos.foro_hub_challenge.infra.exception.UsuarioDuplicadoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDetalle guardarNuevoUsuario(UsuarioCreacionDetalle datos){
        // 1. Busca por email
        repository.findByEmail(datos.email()).ifPresent(usuarioExistente -> {
            throw new UsuarioDuplicadoException("El email ya está registrado.");
        });

        // 2. Hashea la contraseña antes de crear al usuario.
        String contrasenaHash = passwordEncoder.encode(datos.contrasena());

        // 3. Crear al usuario con la contraseña hasheada.
        var usuario = new Usuario(datos.nombre(), datos.email(), contrasenaHash);
        repository.save(usuario);

        return new UsuarioDetalle(usuario);
    }

    public Page<UsuarioDetalle> listarUsuarios(Pageable paginacion){

        return repository.findAll(paginacion)
                .map(UsuarioDetalle::new);
    }

    public UsuarioDetalle listarUsuarioPorId(Long id){

        var usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado."));

        return new UsuarioDetalle(usuario);
    }

    @Transactional
    public UsuarioDetalle actualizarUnUsuario(Long id, UsuarioActualizacionDetalle datos){

        // 1er filtro: buscar el id en DB
        var usuarioExisteId = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado para actualizar.")); // 404

        // 2do filtro: Regla de negocio -> no pueden haber usuarios con el mismo email.
        // Solo busca duplicados si el email es diferente al ingresado.
        if (!usuarioExisteId.getEmail().equals(datos.email())) {

            // Busca que no coincida con otros mails de usuarios ya existentes.
            Optional<Usuario> usuarioDuplicado = repository.findByEmail(datos.email());

            // Si encontramos un usuario con el mismo email...
            if (usuarioDuplicado.isPresent()) {
                // ... Y ese usuario NO es el mismo que estamos actualizando (su ID es diferente).
                if (!usuarioDuplicado.get().getId().equals(id)) {
                    throw new UsuarioDuplicadoException("Email existente en otro usuario."); // 409
                }
            }
        }

        String nuevaContrasenaHash = null;
        // En caso de actualizar la contraseña:
        if (datos.contrasena() != null && !datos.contrasena().isEmpty()) {
            nuevaContrasenaHash = passwordEncoder.encode(datos.contrasena());
        }

        // Pasa todos los filtros
        usuarioExisteId.actualizarUsuario(datos.email(), datos.contrasena());

        return new UsuarioDetalle(usuarioExisteId);
    }

    @Transactional
    public void eliminarUnUsuario(Long id){

        Usuario usuarioExisteId = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado.")); // 404

        repository.delete(usuarioExisteId);
    }
}
