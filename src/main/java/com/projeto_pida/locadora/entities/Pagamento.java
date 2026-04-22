package com.projeto_pida.locadora.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "pagamentos")
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "locacao_id", nullable = false)
    private Locacao locacao;

    private String metodoPagamento; // Ex: CARTAO, PIX, DINHEIRO
    private String statusPagamento; // Ex: PAGO, PENDENTE
    private String tokenTransacao;
    private LocalDateTime dataPagamento;
    private BigDecimal valorPago;
}