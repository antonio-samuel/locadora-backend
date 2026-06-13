package com.projeto_pida.locadora.repositories;

import com.projeto_pida.locadora.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v WHERE v.disponivel = true")
    List<Veiculo> buscarDisponiveis();
}