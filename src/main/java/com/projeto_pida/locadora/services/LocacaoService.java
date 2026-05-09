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

    // 5Criar notificação automática
    // Certifique-se de que a NotificacaoService ou Repository está injetada com @Autowired
   Notificacao nota = new Notificacao();
    nota.setUsuario(locacaoSalva.getUsuario()); // Pega o usuário da locação
    nota.setMensagem("Reserva confirmada: Veículo " + locacaoSalva.getVeiculo().getModelo() + " valor: R$ " + locacaoSalva.getValorTotal());
    nota.setDataEnvio(LocalDateTime.now());
    nota.setLida(false);

    // 3. COMANDO CRUCIAL: Salva no banco de dados
    notificacaoRepository.save(nota); 

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
public Locacao alterar(Long id, Locacao obj) {
    Locacao entidade = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

    // 1. Atualiza as datas básicas (Empréstimo e Prevista)
    if (obj.getDataEmprestimo() != null) {
        entidade.setDataEmprestimo(obj.getDataEmprestimo());
    }
    if (obj.getDataDevolucaoPrevista() != null) {
        entidade.setDataDevolucaoPrevista(obj.getDataDevolucaoPrevista());
    }

    // 2. ATUALIZA A DATA REAL (Esta é a parte que está a faltar!)
    if (obj.getDataDevolucaoReal() != null) {
        entidade.setDataDevolucaoReal(obj.getDataDevolucaoReal());
        
        // Lógica da Multa: Se a real for depois da prevista
        if (entidade.getDataDevolucaoReal().isAfter(entidade.getDataDevolucaoPrevista())) {
            // Calcula 20% sobre o valor total atual
            BigDecimal multa = entidade.getValorTotal().multiply(new BigDecimal("0.20"));
            entidade.setValorMulta(multa);
            
            // Soma a multa ao valor total
            entidade.setValorTotal(entidade.getValorTotal().add(multa));
            entidade.setStatus("CONCLUIDA_COM_ATRASO");
        } else {
            entidade.setStatus("CONCLUIDA_NO_PRAZO");
        }
        
        // Liberta o veículo
        if (entidade.getVeiculo() != null) {
            entidade.getVeiculo().setDisponivel(true);
            veiculoRepository.save(entidade.getVeiculo());
        }
    }

    return repository.save(entidade);
}
    
}