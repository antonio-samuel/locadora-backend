package com.projeto_pida.locadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.projeto_pida.locadora.entities") // Substitua pelo seu pacote exato
@EnableJpaRepositories("com.projeto_pida.locadora.repositories") // Substitua pelo seu pacote exato

@SpringBootApplication
public class LocadoraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocadoraBackendApplication.class, args);

		
	}

}
