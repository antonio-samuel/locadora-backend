package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.entities.Pagamento;
import com.projeto_pida.locadora.repositories.LocacaoRepository;
import com.projeto_pida.locadora.repositories.PagamentoRepository;
import com.projeto_pida.locadora.services.StripeService;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    @PostMapping("/criar/{locacaoId}")
    public ResponseEntity<?> criarPagamento(@PathVariable Long locacaoId) {
        try {
            Locacao locacao = locacaoRepository.findById(locacaoId)
                    .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

            if (!locacao.getStatus().equals("AGUARDANDO_PAGAMENTO")) {
                return ResponseEntity.badRequest()
                        .body("Esta locação não está aguardando pagamento.");
            }

            String url = stripeService.criarSessaoPagamento(locacao);
            return ResponseEntity.ok(Map.of("url", url));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {

        try {
            Event event;

            if (webhookSecret != null && !webhookSecret.isEmpty()) {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } else {
                event = com.stripe.net.ApiResource.GSON.fromJson(payload, Event.class);
            }

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                if (session != null) {
                    String locacaoIdStr = session.getMetadata().get("locacaoId");
                    Long locacaoId = Long.parseLong(locacaoIdStr);

                    Locacao locacao = locacaoRepository.findById(locacaoId).orElse(null);
                    if (locacao != null && locacao.getStatus().equals("AGUARDANDO_PAGAMENTO")) {

                        Pagamento pagamento = new Pagamento();
                        pagamento.setLocacao(locacao);
                        pagamento.setMetodoPagamento("STRIPE");
                        pagamento.setStatusPagamento("PAGO");
                        pagamento.setValorPago(BigDecimal.valueOf(session.getAmountTotal() / 100.0));
                        pagamento.setTokenTransacao(session.getId());
                        pagamento.setDataPagamento(LocalDateTime.now());
                        pagamentoRepository.save(pagamento);

                        if (locacao.getDataDevolucaoReal() != null &&
                            locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucaoPrevista())) {
                            locacao.setStatus("CONCLUIDA_COM_ATRASO");
                        } else {
                            locacao.setStatus("CONCLUIDA_NO_PRAZO");
                        }
                        locacaoRepository.save(locacao);

                        System.out.println("[STRIPE] Locação " + locacaoId + " concluída.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[STRIPE WEBHOOK] Erro: " + e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}