package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Pagamento;
import com.projeto_pida.locadora.services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Libera para o frontend conseguir consumir a API
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll() {
        List<Pagamento> lista = service.listarTodos();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> findById(@PathVariable Long id) {
        Pagamento obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Pagamento> insert(@RequestBody Pagamento obj) {
        obj = service.salvar(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> update(@PathVariable Long id, @RequestBody Pagamento obj) {
        obj = service.alterar(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/processar/{locacaoId}")
public ResponseEntity<?> processar(
        @PathVariable Long locacaoId,
        @RequestBody java.util.Map<String, String> body) {
    try {
        String metodoPagamento = body.get("metodoPagamento");
        Pagamento pagamento = service.processarPagamento(locacaoId, metodoPagamento);
        return ResponseEntity.ok(pagamento);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}
