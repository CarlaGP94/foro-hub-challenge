package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.topico.TopicoService;
import com.aluracursos.foro_hub_challenge.domain.topico.dto.TopicoDetalle;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService service;

    @Transactional
    @PostMapping
    public ResponseEntity<TopicoDetalle> crear(@RequestBody @Valid TopicoCreacionDetalle datos,
                                UriComponentsBuilder uriComponentsBuilder){

        var topico = service.guardarNuevoTopico(datos);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(topico);
    }

    @GetMapping
    public ResponseEntity<Page<TopicoDetalle>> listar(@PageableDefault
                                                          (size = 10,
                                                          sort = {"fechaDeCreacion"},
                                                          direction = Sort.Direction.ASC)
                                                      Pageable paginacion){
        var page = service.listarTopicos(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetalle> detallar(@PathVariable Long id){

        var topico = service.listarTopicoPorId(id);

        return ResponseEntity.ok(topico);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TopicoDetalle> actualizar(@PathVariable Long id,
                                     @RequestBody @Valid TopicoActualizacionDetalle datos){

        var topico = service.actualizarUnTopico(id,datos);

        return ResponseEntity.ok(topico);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<TopicoDetalle> eliminar(@PathVariable Long id){

        service.eliminarUnTopico(id);

        return ResponseEntity.noContent().build(); // Devuelve un 204
    }
}
