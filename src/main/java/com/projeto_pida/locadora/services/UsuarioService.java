package com.projeto_pida.locadora.services;

import com.projeto_pida.locadora.entities.Usuario;
import com.projeto_pida.locadora.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
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
        return obj.orElse(null); // Retorna o usuário ou null se não encontrar
    }

    public Usuario salvar(Usuario usuario) {
        // Pega a senha que veio do Thunder Client, criptografa e define de volta no objeto
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        
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
    
    // Verificação de segurança: só criptografa se houver uma senha enviada
    if (obj.getSenha() != null && !obj.getSenha().isEmpty()) {
        String senhaCriptografada = passwordEncoder.encode(obj.getSenha());
        entidade.setSenha(senhaCriptografada);
    }
    
    entidade.setPerfil(obj.getPerfil());
    entidade.setCnh(obj.getCnh());
    entidade.setCpf(obj.getCpf());
}
}