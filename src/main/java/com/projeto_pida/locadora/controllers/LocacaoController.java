package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.services.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Libera para o frontend conseguir consumir a API
@RequestMapping("/locacoes")
public class LocacaoController {

    @Autowired
    private LocacaoService service;

    @GetMapping
    public ResponseEntity<List<Locacao>> findAll() {
        List<Locacao> lista = service.listarTodas();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locacao> findById(@PathVariable Long id) {
        Locacao obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Locacao> insert(@RequestBody Locacao obj) {
        obj = service.salvar(obj);
        return ResponseEntity.ok().body(obj);
    }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finalizar")
public ResponseEntity<Locacao> finalizar(@PathVariable Long id) {
    Locacao obj = service.finalizarLocacao(id);
    return ResponseEntity.ok().body(obj);
}

@PutMapping("/{id}")
public ResponseEntity<Locacao> update(@PathVariable Long id, @RequestBody Locacao obj) {
    obj = service.alterar(id, obj);
    return ResponseEntity.ok().body(obj);
}
@GetMapping("/usuario/{usuarioId}")
public ResponseEntity<List<Locacao>> findByUsuario(@PathVariable Long usuarioId) {
    List<Locacao> lista = service.listarPorUsuario(usuarioId);
    return ResponseEntity.ok().body(lista);
}
@PutMapping("/{id}/cancelar")
public ResponseEntity<Locacao> cancelar(@PathVariable Long id) {
    try {
        Locacao obj = service.cancelarLocacao(id);
        return ResponseEntity.ok().body(obj);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(null);
    }
}
}