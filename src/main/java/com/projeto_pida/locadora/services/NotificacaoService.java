package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.repositories.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    public List<Notificacao> listarTodos() {
        return repository.findAll();
    }

    public Notificacao buscarPorId(Long id) {
        Optional<Notificacao> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Notificacao salvar(Notificacao notificacao) {
        return repository.save(notificacao);
    }

    public Notificacao alterar(Long id, Notificacao obj) {
        Notificacao entidade = repository.getReferenceById(id);
        entidade.setMensagem(obj.getMensagem());
        entidade.setLida(obj.getLida());
        return repository.save(entidade);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}