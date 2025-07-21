package com.aluracursos.foro_hub_challenge.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestorDeErrores {

    // Maneja la excepción para tópicos duplicados (409 Conflict)
    @ExceptionHandler(TopicoDuplicadoException.class)
    public ResponseEntity gestionarError409(TopicoDuplicadoException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // Maneja la excepción para usuarios duplicados (409 Conflict)
    @ExceptionHandler(UsuarioDuplicadoException.class)
    public ResponseEntity gestionarError409(UsuarioDuplicadoException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // Maneja los errores de validación de los DTOs de entrada (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity gestionarError400(MethodArgumentNotValidException ex){

        var errores = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(errores.stream()
                .map(DatosErrorValidacion::new)
                .toList());
    }

    // Maneja la excepción para recursos no encontrados (404 Not Found) tanto tópicos como usuarios.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity gestionarError404() throws InterruptedException {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID inexistente.");
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public record DatosErrorValidacion(
            String campo,
            String mensaje
    ){
        public DatosErrorValidacion(FieldError fieldError){
            this(
                    fieldError.getField(), // campo que da error.
                    fieldError.getDefaultMessage() // mensaje correspondiente.
            );
        }
    }
}
