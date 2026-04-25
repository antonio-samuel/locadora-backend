package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Veiculo;
import com.projeto_pida.locadora.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository repository;

    public List<Veiculo> listarTodos() {
        return repository.findAll();
    }

    public Veiculo buscarPorId(Long id) {
        Optional<Veiculo> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Veiculo salvar(Veiculo veiculo) {
        return repository.save(veiculo);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Veiculo alterar(Long id, Veiculo obj) {
    Veiculo entidade = repository.getReferenceById(id);
    entidade.setModelo(obj.getModelo());
    entidade.setMarca(obj.getMarca());
    entidade.setCor(obj.getCor());
    entidade.setAno(obj.getAno());
    entidade.setValorDiaria(obj.getValorDiaria());
    entidade.setDisponivel(obj.getDisponivel());
    return repository.save(entidade);
}
}