package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Usuario;
import com.projeto_pida.locadora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> obj = repository.findById(id);
        return obj.orElse(null); // Retorna o usuário ou null se não encontrar
    }

    public Usuario salvar(Usuario usuario) {
        // Aqui futuramente você pode colocar a lógica de criptografia de senha
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}