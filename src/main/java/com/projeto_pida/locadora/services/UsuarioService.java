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
        // Aqui futuramente colocar a lógica de criptografia de senha
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Usuario alterar(Long id, Usuario obj) {
    Usuario entidade = repository.getReferenceById(id); // Prepara o objeto monitorado pelo JPA
    atualizarDados(entidade, obj);
    return repository.save(entidade);
}

private void atualizarDados(Usuario entidade, Usuario obj) {
    entidade.setNome(obj.getNome());
    entidade.setEmail(obj.getEmail());
    entidade.setSenha(obj.getSenha());
    entidade.setPerfil(obj.getPerfil());
    entidade.setCnh(obj.getCnh());
    entidade.setCpf(obj.getCpf());
    // Adicione outros campos que deseja permitir a alteração (CPF e CNH geralmente não mudam)
}
}