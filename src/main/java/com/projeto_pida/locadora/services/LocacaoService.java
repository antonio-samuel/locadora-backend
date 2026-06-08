package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Locacao;
import com.projeto_pida.locadora.entities.Notificacao;
import com.projeto_pida.locadora.entities.Veiculo;
import com.projeto_pida.locadora.repositories.LocacaoRepository;
import com.projeto_pida.locadora.repositories.NotificacaoRepository;
import com.projeto_pida.locadora.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository repository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public List<Locacao> listarTodas() {
        return repository.findAll();
    }

    public Locacao buscarPorId(Long id) {
        Optional<Locacao> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    // ── Cálculo de multa proporcional aos dias de atraso ──────────────────────
    // Fórmula: diasAtraso × valorDiaria × 0.20
    // Exemplo: 3 dias de atraso, diária R$100 → multa = 3 × 100 × 0.20 = R$60
    private BigDecimal calcularMulta(Locacao locacao, LocalDateTime dataReal) {
        long diasAtraso = ChronoUnit.DAYS.between(
            locacao.getDataDevolucaoPrevista(), dataReal
        );
        if (diasAtraso <= 0) return BigDecimal.ZERO;

        BigDecimal valorDiaria = BigDecimal.valueOf(
            locacao.getVeiculo().getValorDiaria()
        );
        return valorDiaria
            .multiply(new BigDecimal(diasAtraso))
            .multiply(new BigDecimal("0.02"));
    }

    public Locacao salvar(Locacao locacao) {
        // 1. Busca o veículo e valida
        Veiculo veiculo = veiculoRepository.findById(locacao.getVeiculo().getId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        // 2. Cálculo de dias e valor total
        long dias = ChronoUnit.DAYS.between(
            locacao.getDataEmprestimo(), locacao.getDataDevolucaoPrevista()
        );
        if (dias <= 0) dias = 1;

        BigDecimal valorDiaria = BigDecimal.valueOf(veiculo.getValorDiaria());
        BigDecimal valorTotal  = valorDiaria.multiply(new BigDecimal(dias));
        locacao.setValorTotal(valorTotal);

        // 3. Marca veículo como indisponível
        veiculo.setDisponivel(false);
        veiculoRepository.save(veiculo);

        // 4. Salva a locação
        Locacao locacaoSalva = repository.save(locacao);

        // 5. Cria notificação automática
        Notificacao nota = new Notificacao();
        nota.setUsuario(locacaoSalva.getUsuario());
        nota.setMensagem("Reserva confirmada: Veículo "
            + locacaoSalva.getVeiculo().getModelo()
            + " — Valor: R$ " + locacaoSalva.getValorTotal());
        nota.setDataEnvio(LocalDateTime.now());
        nota.setLida(false);
        notificacaoRepository.save(nota);

        return locacaoSalva;
    }
public Locacao finalizarLocacao(Long id) {
    Locacao locacao = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

    LocalDateTime dataReal = LocalDateTime.now();
    locacao.setDataDevolucaoReal(dataReal);

    BigDecimal valorDiaria = BigDecimal.valueOf(locacao.getVeiculo().getValorDiaria());

    if (dataReal.isAfter(locacao.getDataDevolucaoPrevista())) {
        BigDecimal multa = calcularMulta(locacao, dataReal);
        locacao.setValorMulta(multa);
        locacao.setValorTotal(locacao.getValorTotal().add(multa));
    } else if (dataReal.isBefore(locacao.getDataDevolucaoPrevista())) {
        long diasUsados = ChronoUnit.DAYS.between(
            locacao.getDataEmprestimo(), dataReal
        );
        if (diasUsados <= 0) diasUsados = 1;
        BigDecimal novoValor = valorDiaria.multiply(new BigDecimal(diasUsados));
        locacao.setValorTotal(novoValor);
        locacao.setValorMulta(BigDecimal.ZERO);
    } else {
        locacao.setValorMulta(BigDecimal.ZERO);
    }

    // Veículo liberado mas aguarda pagamento antes de concluir
    locacao.setStatus("AGUARDANDO_PAGAMENTO");

    Veiculo v = locacao.getVeiculo();
    v.setDisponivel(true);
    veiculoRepository.save(v);

    return repository.save(locacao);
}

    public Locacao alterar(Long id, Locacao obj) {
        Locacao entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

        if (obj.getDataEmprestimo() != null) {
            entidade.setDataEmprestimo(obj.getDataEmprestimo());
        }
        if (obj.getDataDevolucaoPrevista() != null) {
            entidade.setDataDevolucaoPrevista(obj.getDataDevolucaoPrevista());
        }

        if (obj.getDataDevolucaoReal() != null) {
    entidade.setDataDevolucaoReal(obj.getDataDevolucaoReal());

    BigDecimal valorDiaria = BigDecimal.valueOf(
        entidade.getVeiculo().getValorDiaria()
    );

    if (entidade.getDataDevolucaoReal().isAfter(entidade.getDataDevolucaoPrevista())) {
        // Atraso
        BigDecimal multa = calcularMulta(entidade, entidade.getDataDevolucaoReal());
        entidade.setValorMulta(multa);
        entidade.setValorTotal(entidade.getValorTotal().add(multa));
        entidade.setStatus("CONCLUIDA_COM_ATRASO");

    } else if (entidade.getDataDevolucaoReal().isBefore(entidade.getDataDevolucaoPrevista())) {
        // Antecipada
        long diasUsados = ChronoUnit.DAYS.between(
            entidade.getDataEmprestimo(), entidade.getDataDevolucaoReal()
        );
        if (diasUsados <= 0) diasUsados = 1;

        BigDecimal novoValor = valorDiaria.multiply(new BigDecimal(diasUsados));
        entidade.setValorTotal(novoValor);
        entidade.setValorMulta(BigDecimal.ZERO);
        entidade.setStatus("CONCLUIDA_NO_PRAZO");

    } else {
        // Exatamente no prazo
        entidade.setValorMulta(BigDecimal.ZERO);
        entidade.setStatus("CONCLUIDA_NO_PRAZO");
    }

    if (entidade.getVeiculo() != null) {
        entidade.getVeiculo().setDisponivel(true);
        veiculoRepository.save(entidade.getVeiculo());
    }
}
        return repository.save(entidade);
    }

    public List<Locacao> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public Locacao cancelarLocacao(Long id) {
        Locacao locacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

        if (!locacao.getStatus().equals("ATIVA")) {
            throw new RuntimeException("Apenas locações ativas podem ser canceladas.");
        }

        Veiculo veiculo = locacao.getVeiculo();
        veiculo.setDisponivel(true);
        veiculoRepository.save(veiculo);

        locacao.setStatus("CANCELADA");
        return repository.save(locacao);
    }
}