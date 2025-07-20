package com.aluracursos.foro_hub_challenge.infra.exception;

public class ValidacionException extends RuntimeException{

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
