package com.aluracursos.foro_hub_challenge.infra.exception;

public class UsuarioDuplicadoException extends RuntimeException{

    public UsuarioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
