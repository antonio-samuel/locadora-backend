package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.entities.Pagamento;
import com.projeto_pida.locadora.repositories.LocacaoRepository;
import com.projeto_pida.locadora.repositories.PagamentoRepository;
import com.projeto_pida.locadora.services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mercadopago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    /**
     * Frontend chama este endpoint para obter o link de pagamento do Mercado Pago.
     * Retorna a URL que o Angular vai usar para redirecionar o usuário.
     */
    @PostMapping("/criar/{locacaoId}")
    public ResponseEntity<?> criarPagamento(@PathVariable Long locacaoId) {
        try {
            Locacao locacao = locacaoRepository.findById(locacaoId)
                    .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

            if (!locacao.getStatus().equals("AGUARDANDO_PAGAMENTO")) {
                return ResponseEntity.badRequest()
                        .body("Esta locação não está aguardando pagamento.");
            }

            String urlPagamento = mercadoPagoService.criarPreferenciaPagamento(locacao);
            return ResponseEntity.ok(Map.of("url", urlPagamento));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Webhook — o Mercado Pago chama este endpoint automaticamente
     * quando o pagamento é confirmado, rejeitado ou fica pendente.
     */
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "data.id", required = false) String dataId,
            @RequestBody(required = false) Map<String, Object> body) {

        System.out.println("[WEBHOOK] type=" + type + " | dataId=" + dataId);

        // Só processa notificações de pagamento aprovado
        if ("payment".equals(type) && dataId != null) {
            try {
                // Busca o pagamento no Mercado Pago pelo ID
                com.mercadopago.client.payment.PaymentClient client =
                    new com.mercadopago.client.payment.PaymentClient();
                com.mercadopago.resources.payment.Payment payment = client.get(Long.parseLong(dataId));

                String status          = payment.getStatus(); // approved, rejected, pending
                String externalRef     = payment.getExternalReference(); // ID da nossa locação
                BigDecimal valorPago   = payment.getTransactionAmount();

                System.out.println("[WEBHOOK] status=" + status + " | locacaoId=" + externalRef);

                if ("approved".equals(status) && externalRef != null) {
                    Long locacaoId = Long.parseLong(externalRef);

                    Locacao locacao = locacaoRepository.findById(locacaoId).orElse(null);
                    if (locacao != null && locacao.getStatus().equals("AGUARDANDO_PAGAMENTO")) {

                        // Salva o pagamento
                        Pagamento pagamento = new Pagamento();
                        pagamento.setLocacao(locacao);
                        pagamento.setMetodoPagamento("MERCADO_PAGO");
                        pagamento.setStatusPagamento("PAGO");
                        pagamento.setValorPago(valorPago);
                        pagamento.setTokenTransacao(dataId);
                        pagamento.setDataPagamento(LocalDateTime.now());
                        pagamentoRepository.save(pagamento);

                        // Conclui a locação
                        if (locacao.getDataDevolucaoReal() != null &&
                            locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucaoPrevista())) {
                            locacao.setStatus("CONCLUIDA_COM_ATRASO");
                        } else {
                            locacao.setStatus("CONCLUIDA_NO_PRAZO");
                        }
                        locacaoRepository.save(locacao);

                        System.out.println("[WEBHOOK] Locação " + locacaoId + " concluída com sucesso.");
                    }
                }
            } catch (Exception e) {
                System.err.println("[WEBHOOK] Erro: " + e.getMessage());
            }
        }

        // Sempre retorna 200 para o Mercado Pago não reenviar a notificação
        return ResponseEntity.ok().build();
    }
}