package com.aluracursos.foro_hub_challenge.infra.security;

import com.aluracursos.foro_hub_challenge.domain.usuario.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter { // Filter propio de Spring (similar Filter jakarta)

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request); // obtiene el token en uso
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // obtiene al usuario que está usando el token
            var usuario = usuarioRepository.findByNombre(subject); // busca a este usuario en el repository

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication); // Le dice a Spring Security que el usuario está realmente autenticado.
        }

        filterChain.doFilter(request, response); // continua con la cadena de filtros
    }

    private String recuperarToken(HttpServletRequest request) {
        // recupera el token -> Header (Authorization)
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", ""); // quita el prefijo para obtener solo el token
        }
        return null;
    }
}
