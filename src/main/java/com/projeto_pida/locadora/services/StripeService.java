package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Locacao;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
        System.out.println("[STRIPE] Configurado com sucesso.");
    }

    public String criarSessaoPagamento(Locacao locacao) {
        try {
            // Converte o valor para centavos (Stripe usa centavos)
            long valorEmCentavos = locacao.getValorTotal()
                    .multiply(new java.math.BigDecimal("100"))
                    .longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/dashboard?pagamento=sucesso&locacaoId=" + locacao.getId())
                    .setCancelUrl("http://localhost:4200/dashboard?pagamento=cancelado")
                    .addLineItem(
                        SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("brl")
                                    .setUnitAmount(valorEmCentavos)
                                    .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Aluguel - " + locacao.getVeiculo().getMarca()
                                                + " " + locacao.getVeiculo().getModelo())
                                            .setDescription("Placa: " + locacao.getVeiculo().getPlaca())
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .putMetadata("locacaoId", locacao.getId().toString())
                    .build();

            Session session = Session.create(params);
            System.out.println("[STRIPE] Sessão criada: " + session.getId());
            return session.getUrl();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento no Stripe: " + e.getMessage());
        }
    }
}