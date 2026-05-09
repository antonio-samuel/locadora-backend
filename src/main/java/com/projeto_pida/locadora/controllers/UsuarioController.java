package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Usuario;
import com.projeto_pida.locadora.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // Listar todos os usuários: GET http://localhost:8080/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> lista = service.listarTodos();
        return ResponseEntity.ok().body(lista);
    }

    // Buscar por ID: GET http://localhost:8080/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Usuario obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    // Criar novo usuário: POST http://localhost:8080/usuarios
    @PostMapping
    public ResponseEntity<Usuario> insert(@RequestBody Usuario obj) {
        obj = service.salvar(obj);
        return ResponseEntity.ok().body(obj);
    }
    
    // Alterar usuário: PUT http://localhost:8081/usuarios/1
@PutMapping("/{id}")
public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario obj) {
    obj = service.alterar(id, obj);
    return ResponseEntity.ok().body(obj);
}

 @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}