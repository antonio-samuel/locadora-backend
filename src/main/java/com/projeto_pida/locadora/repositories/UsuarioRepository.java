package com.projeto_pida.locadora.repositories;

import com.projeto_pida.locadora.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByCnh(String cnh);
    Optional<Usuario> findByTelefone(String telefone);
    Optional<Usuario> findByEmail(String email);
}