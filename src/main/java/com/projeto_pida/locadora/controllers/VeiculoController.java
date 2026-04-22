package com.projeto_pida.locadora.controllers;

import com.projeto_pida.locadora.entities.Veiculo;
import com.projeto_pida.locadora.services.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    @Autowired
    private VeiculoService service;

    @GetMapping
    public List<Veiculo> listar() { return service.listarTodos(); }

    @PostMapping
    public Veiculo cadastrar(@RequestBody Veiculo v) { return service.salvar(v); }
}