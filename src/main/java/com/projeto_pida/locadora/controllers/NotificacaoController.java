package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.services.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @GetMapping
    public ResponseEntity<List<Notificacao>> findAll() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> findByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<List<Notificacao>> findNaoLidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarNaoLidasPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/contador")
    public ResponseEntity<Long> contarNaoLidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.contarNaoLidas(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Notificacao> insert(@RequestBody Notificacao obj) {
        return ResponseEntity.ok(service.salvar(obj));
    }

    @PutMapping("/{id}/lida")
    public ResponseEntity<Notificacao> marcarLida(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarComoLida(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> update(@PathVariable Long id, @RequestBody Notificacao obj) {
        return ResponseEntity.ok(service.alterar(id, obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}