package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Pagamento;
import com.projeto_pida.locadora.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    public List<Pagamento> listarTodos() {
        return repository.findAll();
    }

    public Pagamento buscarPorId(Long id) {
        Optional<Pagamento> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Pagamento salvar(Pagamento pagamento) {
        // Aqui você pode adicionar regras de negócio, como validar se a locação existe
        return repository.save(pagamento);
    }

    public Pagamento alterar(Long id, Pagamento obj) {
        Pagamento entidade = repository.getReferenceById(id);
        entidade.setMetodoPagamento(obj.getMetodoPagamento());
        entidade.setStatusPagamento(obj.getStatusPagamento());
        entidade.setValorPago(obj.getValorPago());
        entidade.setDataPagamento(obj.getDataPagamento());
        return repository.save(entidade);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}