package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.repositories.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificacaoScheduler {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Roda todo dia às 08:00.
     * Busca locações com devolução prevista para amanhã e notifica o cliente.
     */
    @Scheduled(cron = "0 * * * * *")
    public void notificarVencimentoAmanha() {
        LocalDateTime amanha      = LocalDateTime.now().plusDays(1);
        LocalDateTime amanhaInicio = amanha.toLocalDate().atStartOfDay();
        LocalDateTime amanhaFim    = amanhaInicio.plusDays(1).minusSeconds(1);

        List<Locacao> locacoesVencendo = locacaoRepository
                .findByStatusAndDataDevolucaoPrevistasBetween("ATIVA", amanhaInicio, amanhaFim);

        for (Locacao locacao : locacoesVencendo) {
            String mensagem = String.format(
                "⏰ Lembrete: a devolução do veículo %s %s (placa %s) está prevista para amanhã, %s. " +
                "Evite multas devolvendo no prazo!",
                locacao.getVeiculo().getMarca(),
                locacao.getVeiculo().getModelo(),
                locacao.getVeiculo().getPlaca(),
                locacao.getDataDevolucaoPrevista().toLocalDate()
            );

            // Salva notificação no banco
            Notificacao nota = new Notificacao();
            nota.setUsuario(locacao.getUsuario());
            nota.setMensagem(mensagem);
            nota.setDataEnvio(LocalDateTime.now());
            nota.setLida(false);
            notificacaoService.salvar(nota);

            // Envia email
            notificacaoService.enviarEmail(
                locacao.getUsuario().getEmail(),
                "LocaDrive — Lembrete de devolução",
                mensagem
            );
        }

        System.out.println("[SCHEDULER] Notificações enviadas para " +
            locacoesVencendo.size() + " locação(ões) vencendo amanhã.");
    }
}