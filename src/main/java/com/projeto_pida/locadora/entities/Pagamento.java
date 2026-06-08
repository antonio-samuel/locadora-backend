package com.projeto_pida.locadora.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String metodoPagamento;
    private String statusPagamento;
    private String tokenTransacao;
    private LocalDateTime dataPagamento;
    private BigDecimal valorPago;
}