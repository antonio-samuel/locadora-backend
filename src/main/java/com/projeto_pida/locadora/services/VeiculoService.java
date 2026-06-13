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
    public Veiculo salvar(Veiculo obj) {
    
    return repository.save(obj);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
    
    public List<Veiculo> listarDisponiveis() {
    return repository.buscarDisponiveis();
}
   public Veiculo alterar(Long id, Veiculo obj) {
    Veiculo entidade = repository.findById(id).orElseThrow();
    entidade.setMarca(obj.getMarca());
    entidade.setModelo(obj.getModelo());
    entidade.setPlaca(obj.getPlaca());
    entidade.setCor(obj.getCor());
    entidade.setAno(obj.getAno());
    entidade.setValorDiaria(obj.getValorDiaria());
    entidade.setDisponivel(obj.getDisponivel());
    entidade.setFotoUrl(obj.getFotoUrl());   // 👈 imprescindível
   entidade.setCategoria(obj.getCategoria());
    return repository.save(entidade);
}
}