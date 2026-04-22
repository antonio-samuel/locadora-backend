package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Veiculo;
import com.projeto_pida.locadora.repositories.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository repository;

    public List<Veiculo> listarTodos() { return repository.findAll(); }
    public Veiculo salvar(Veiculo v) { return repository.save(v); }
}