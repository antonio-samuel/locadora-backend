package com.projeto_pida.locadora.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.projeto_pida.locadora.entities.Locacao;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {
    
    

    @Value("${mercadopago.access.token}")
    private String accessToken;

    // Configura o token assim que o Spring Boot iniciar este serviço
    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
         System.out.println("[MP] Access Token carregado: " + accessToken);
  
    }

    public String criarPreferenciaPagamento(Locacao locacao) {
            System.out.println("[MP] Token: " + accessToken);
            System.out.println("[MP] Locacao ID: " + locacao.getId());
            System.out.println("[MP] Valor: " + locacao.getValorTotal());
        try {
            PreferenceClient client = new PreferenceClient();

            // 1. Monta o item que está sendo "comprado" (O aluguel do carro)
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Aluguel - " + locacao.getVeiculo().getMarca() + " " + locacao.getVeiculo().getModelo())
                    .quantity(1)
                    .unitPrice(locacao.getValorTotal())
                    .currencyId("BRL")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(item);

            // 2. Para onde o Mercado Pago deve redirecionar o usuário após pagar?
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:4200/dashboard?pagamento=sucesso")
                    .failure("http://localhost:4200/dashboard?pagamento=falha")
                    .pending("http://localhost:4200/dashboard?pagamento=pendente")
                    .build();

            // 3. Junta tudo no Payload (Preferencia)
            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved") // Retorna automaticamente pro Angular se aprovar
                    .externalReference(locacao.getId().toString()) // Guarda o ID da locação no Mercado Pago
                    .build();

            // 4. Envia pro Mercado Pago e recebe a resposta
            Preference preference = client.create(request);

            // Retorna o link de pagamento que o Angular vai usar para abrir a tela
            return preference.getInitPoint(); 

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento no Mercado Pago: " + e.getMessage());
        }
    }
}