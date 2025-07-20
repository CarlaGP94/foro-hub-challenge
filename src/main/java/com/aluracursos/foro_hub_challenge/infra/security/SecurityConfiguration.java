package com.aluracursos.foro_hub_challenge.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // se deshabilitan funciones Stateful.
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST,"/login").permitAll() // request login con metodo post -> envía datos -> acceso ok
                                //.requestMatchers("v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll() // libera url para spring docs.
                                .anyRequest().authenticated() // si no está logueado, no puede hacer otras request.
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //agregá un filtro (el nuestro) antes que otro (el de spring)
                .build();
    }

    @Bean // devuelve un AuthenticationManager para ser reconocido por el @Autowired
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // Especifica que las contraseñas tienen el hashin de BCrypt
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
