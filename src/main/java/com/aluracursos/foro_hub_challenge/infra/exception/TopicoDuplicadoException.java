package com.aluracursos.foro_hub_challenge.infra.exception;

public class TopicoDuplicadoException extends RuntimeException{

    public TopicoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
