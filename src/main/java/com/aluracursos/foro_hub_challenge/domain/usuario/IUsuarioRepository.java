package com.aluracursos.foro_hub_challenge.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario,Long> {

    UserDetails findByNombre(String username);

    Optional<Usuario> findByEmail(String email);
}
