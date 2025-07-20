package com.aluracursos.foro_hub_challenge.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario,Long> {
    UserDetails findByNombre(String username);

    Usuario findByEmail(String email);

    Optional<Usuario> getReferenceByEmail(String email);
}
