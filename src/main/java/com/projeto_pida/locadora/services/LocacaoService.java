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

    private NotificacaoRepository notificacaoRepository;
    public Locacao salvar(Locacao locacao) {
    // 1. Busca o veículo e valida
    Veiculo veiculo = veiculoRepository.findById(locacao.getVeiculo().getId())
            .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

    // 2. Cálculo de dias e Valor Total
    long dias = ChronoUnit.DAYS.between(locacao.getDataEmprestimo(), locacao.getDataDevolucaoPrevista());
    if (dias <= 0) dias = 1;

    BigDecimal valorDiaria = BigDecimal.valueOf(veiculo.getValorDiaria());
    BigDecimal valorTotal = valorDiaria.multiply(new BigDecimal(dias));
    locacao.setValorTotal(valorTotal);

    // 3. Atualiza status do veículo
    veiculo.setDisponivel(false);
    veiculoRepository.save(veiculo);

    // 4. Salva a locação no banco (AQUI VOCÊ SALVA UMA VEZ SÓ)
    Locacao locacaoSalva = repository.save(locacao);

    // 5 Criar notificação automática
    // Certifique-se de que a NotificacaoService ou Repository está injetada com @Autowired
    Notificacao nota = new Notificacao();
    nota.setUsuario(locacaoSalva.getUsuario());
    nota.setMensagem("Sua locação do veículo ID " + locacaoSalva.getVeiculo().getId() + " foi confirmada!");
    nota.setDataEnvio(LocalDateTime.now());
    nota.setLida(false);
    notificacaoRepository.save(nota);

    // 6. Retorno único no final do método
    return locacaoSalva;

    }

    public Locacao realizarLocacao(Locacao locacao) {
        Veiculo veiculo = veiculoRepository.findById(locacao.getVeiculo().getId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
        if (!veiculo.getDisponivel()) {
            throw new RuntimeException("Este veículo já está alugado no momento!");
        }
        
        return salvar(locacao);
    }

    public Locacao finalizarLocacao(Long id) {
        Locacao locacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada"));
        
        locacao.setDataDevolucaoReal(LocalDateTime.now());
        
        if (locacao.getDataDevolucaoReal().isAfter(locacao.getDataDevolucaoPrevista())) {
            BigDecimal multa = locacao.getValorTotal().multiply(new BigDecimal("0.20"));
            locacao.setValorMulta(multa);
            locacao.setValorTotal(locacao.getValorTotal().add(multa));
        }

        Veiculo v = locacao.getVeiculo();
        v.setDisponivel(true);
        veiculoRepository.save(v);

        return repository.save(locacao);
    }

    
}