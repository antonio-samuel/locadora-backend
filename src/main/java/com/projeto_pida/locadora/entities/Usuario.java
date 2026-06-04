package com.projeto_pida.locadora.entities;

import com.projeto_pida.locadora.enums.Perfil;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data // O Lombok gera getters e setters
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String cnh;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 11) 
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;
}