package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Veiculo;
import com.projeto_pida.locadora.services.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Libera para o frontend conseguir consumir a API
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService service;

    // Listar todos: GET http://localhost:8081/veiculos
    @GetMapping
    public ResponseEntity<List<Veiculo>> findAll() {
        List<Veiculo> lista = service.listarTodos();
        return ResponseEntity.ok().body(lista);
    }

    // Buscar por ID: GET http://localhost:8081/veiculos/1
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> findById(@PathVariable Long id) {
        Veiculo obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    // Criar novo: POST http://localhost:8081/veiculos
    @PostMapping
    public ResponseEntity<Veiculo> insert(@RequestBody Veiculo obj) {
        obj = service.salvar(obj);
        return ResponseEntity.ok().body(obj);
    }
    
    // Deletar: DELETE http://localhost:8081/veiculos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
    // Alterar veículo: PUT http://localhost:8081/veiculos/1
@PutMapping("/{id}")
public ResponseEntity<Veiculo> update(@PathVariable Long id, @RequestBody Veiculo obj) {
    obj = service.alterar(id, obj);
    return ResponseEntity.ok().body(obj);
}
}