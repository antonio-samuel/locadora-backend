package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.services.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @GetMapping
    public ResponseEntity<List<Notificacao>> findAll() {
        List<Notificacao> lista = service.listarTodos();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> findById(@PathVariable Long id) {
        Notificacao obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Notificacao> insert(@RequestBody Notificacao obj) {
        obj = service.salvar(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> update(@PathVariable Long id, @RequestBody Notificacao obj) {
        obj = service.alterar(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}