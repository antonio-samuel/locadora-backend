package com.projeto_pida.locadora.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "veiculos")
@Data
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String placa;
    private String cor;
    private Integer ano;
    private Double valorDiaria;
    private Boolean disponivel;
     private String fotoUrl;
     private String categoria;   // ex.: HATCH, SUV, SEDAN, PICKUP
}