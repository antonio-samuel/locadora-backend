package com.projeto_pida.locadora.repositories;

import com.projeto_pida.locadora.entities.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
List<Locacao> findByUsuarioId(Long usuarioId);
@Query("SELECT l FROM Locacao l WHERE l.status = :status " +
       "AND l.dataDevolucaoPrevista BETWEEN :inicio AND :fim")
List<Locacao> findByStatusAndDataDevolucaoPrevistasBetween(
    @Param("status") String status,
    @Param("inicio") LocalDateTime inicio,
    @Param("fim") LocalDateTime fim
);
}