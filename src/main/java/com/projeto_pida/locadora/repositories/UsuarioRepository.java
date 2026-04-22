package com.projeto_pida.locadora.repositories;

import com.projeto_pida.locadora.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Pronto! Aqui você já tem save, delete, findById, etc.
}