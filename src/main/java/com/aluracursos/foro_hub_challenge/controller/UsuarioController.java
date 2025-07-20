package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.usuario.UsuarioService;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioActualizacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioCreacionDetalle;
import com.aluracursos.foro_hub_challenge.domain.usuario.dto.UsuarioDetalle;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Transactional
    @PostMapping
    public ResponseEntity<UsuarioDetalle> crear(@RequestBody @Valid UsuarioCreacionDetalle datos,
                                                UriComponentsBuilder uriComponentsBuilder){

        var usuario = service.guardarNuevoUsuario(datos);

        var uri = uriComponentsBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDetalle>> listar(@PageableDefault
                                                              (size = 10,
                                                                      sort = {"nombre"},
                                                                      direction = Sort.Direction.ASC)
                                                      Pageable paginacion){
        var page = service.listarUsuarios(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalle> detallar(@PathVariable Long id){

        var usuario = service.listarUsuarioPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetalle> actualizar(@PathVariable Long id,
                                                    @RequestBody @Valid UsuarioActualizacionDetalle datos){

        var usuario = service.actualizarUnUsuario(id,datos);

        return ResponseEntity.ok(usuario);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDetalle> eliminar(@PathVariable Long id){

        service.eliminarUnUsuario(id);

        return ResponseEntity.noContent().build(); // Devuelve un 204
    }
}
