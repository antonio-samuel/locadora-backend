package com.projeto_pida.locadora.repositories;

import com.projeto_pida.locadora.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
     List<Notificacao> findByUsuarioId(Long usuarioId);
    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);
    long countByUsuarioIdAndLidaFalse(Long usuarioId);
}