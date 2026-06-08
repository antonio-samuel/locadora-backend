package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.entities.Pagamento;
import com.projeto_pida.locadora.repositories.LocacaoRepository;
import com.projeto_pida.locadora.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private LocacaoRepository locacaoRepository;

    public List<Pagamento> listarTodos() {
        return repository.findAll();
    }

    public Pagamento buscarPorId(Long id) {
        Optional<Pagamento> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Pagamento salvar(Pagamento pagamento) {
        return repository.save(pagamento);
    }

    /**
     * Processa o pagamento de uma locação:
     * 1. Salva o pagamento com status PAGO
     * 2. Atualiza o status da locação para CONCLUIDA_NO_PRAZO ou CONCLUIDA_COM_ATRASO
     */
    public Pagamento processarPagamento(Long locacaoId, String metodoPagamento) {
        Locacao locacao = locacaoRepository.findById(locacaoId)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

        if (!locacao.getStatus().equals("AGUARDANDO_PAGAMENTO")) {
            throw new RuntimeException("Esta locação não está aguardando pagamento.");
        }

        // Cria e salva o pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setLocacao(locacao);
        pagamento.setMetodoPagamento(metodoPagamento);
        pagamento.setStatusPagamento("PAGO");
        pagamento.setValorPago(locacao.getValorTotal());
        pagamento.setDataPagamento(LocalDateTime.now());
        Pagamento pagamentoSalvo = repository.save(pagamento);

        // Conclui a locação automaticamente
        if (locacao.getDataDevolucaoReal() != null &&
            locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucaoPrevista())) {
            locacao.setStatus("CONCLUIDA_COM_ATRASO");
        } else {
            locacao.setStatus("CONCLUIDA_NO_PRAZO");
        }
        locacaoRepository.save(locacao);

        return pagamentoSalvo;
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