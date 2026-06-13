package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.repositories.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Notificacao> listarTodos() {
        return repository.findAll();
    }

    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Notificacao> listarNaoLidasPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndLidaFalse(usuarioId);
    }

    public long contarNaoLidas(Long usuarioId) {
        return repository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    public Notificacao buscarPorId(Long id) {
        Optional<Notificacao> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Notificacao salvar(Notificacao notificacao) {
        return repository.save(notificacao);
    }

    public Notificacao marcarComoLida(Long id) {
        Notificacao notificacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notificacao.setLida(true);
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

    /**
     * Envia email de notificação para o usuário.
     * Falha silenciosamente para não quebrar o fluxo principal.
     */
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("SEU_EMAIL@gmail.com");
            email.setTo(destinatario);
            email.setSubject(assunto);
            email.setText(mensagem);
            mailSender.send(email);
        } catch (Exception e) {
            System.err.println("[EMAIL] Falha ao enviar para " + destinatario + ": " + e.getMessage());
        }
    }
}