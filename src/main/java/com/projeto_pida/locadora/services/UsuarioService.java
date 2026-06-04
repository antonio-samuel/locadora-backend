package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Usuario;
import com.projeto_pida.locadora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.projeto_pida.locadora.config.*;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> obj = repository.findById(id);
        return obj.orElse(null);
    }

    public Usuario salvar(Usuario usuario) {

        if (repository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new RuntimeException("DUPLICADO_CPF");
        }

        if (repository.findByCnh(usuario.getCnh()).isPresent()) {
            throw new RuntimeException("DUPLICADO_CNH");
        }

        if (usuario.getTelefone() != null && !usuario.getTelefone().isEmpty()) {
    if (repository.findByTelefone(usuario.getTelefone()).isPresent()) {
        throw new RuntimeException("DUPLICADO_TELEFONE");
    }
}

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("DUPLICADO_EMAIL");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Usuario alterar(Long id, Usuario obj) {
    Usuario entidade = repository.getReferenceById(id);

    // Verifica se o CPF pertence a outro usuário
    repository.findByCpf(obj.getCpf())
        .filter(u -> !u.getId().equals(id))
        .ifPresent(u -> { throw new RuntimeException("DUPLICADO_CPF"); });

    // Verifica se a CNH pertence a outro usuário
    repository.findByCnh(obj.getCnh())
        .filter(u -> !u.getId().equals(id))
        .ifPresent(u -> { throw new RuntimeException("DUPLICADO_CNH"); });

    // Verifica se o e-mail pertence a outro usuário
    repository.findByEmail(obj.getEmail())
        .filter(u -> !u.getId().equals(id))
        .ifPresent(u -> { throw new RuntimeException("DUPLICADO_EMAIL"); });

    // Verifica se o telefone pertence a outro usuário
    if (obj.getTelefone() != null && !obj.getTelefone().isEmpty()) {
    repository.findByTelefone(obj.getTelefone())
        .filter(u -> !u.getId().equals(id))
        .ifPresent(u -> { throw new RuntimeException("DUPLICADO_TELEFONE"); });
}

    atualizarDados(entidade, obj);
    return repository.save(entidade);
}

   private void atualizarDados(Usuario entidade, Usuario obj) {
    entidade.setNome(obj.getNome());
    entidade.setEmail(obj.getEmail());
    entidade.setCpf(obj.getCpf());
    entidade.setCnh(obj.getCnh());
    entidade.setTelefone(obj.getTelefone());
    entidade.setPerfil(obj.getPerfil());

    if (obj.getSenha() != null && !obj.getSenha().isEmpty()) {
        String senhaCriptografada = passwordEncoder.encode(obj.getSenha());
        entidade.setSenha(senhaCriptografada);
    }
}
}