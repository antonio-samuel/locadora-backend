🚗 LocaDrive — Backend (API REST)
O LocaDrive Backend é uma API RESTful corporativa desenvolvida em Java com Spring Boot, responsável por gerenciar toda a lógica de negócios, persistência de dados e segurança de uma plataforma completa de locação de veículos.

🛠️ Tecnologias Utilizadas
Linguagem: Java 21 LTS

Framework: Spring Boot 3.5

Segurança & Criptografia: Spring Security & BCrypt (para hashing seguro de senhas)

Persistência de Dados: Spring Data JPA / Hibernate

Banco de Dados: MySQL

Gerenciador de Dependências: Maven

Utilitário de Código: Lombok

Integração de Pagamento: Stripe API

📋 Pré-requisitos
Antes de iniciar a aplicação, certifique-se de ter instalado em sua máquina:

JDK 21 ou superior

MySQL Server 8.0+

Git

Uma IDE de sua preferência (IntelliJ IDEA, Eclipse ou VS Code)

⚙️ Configuração do Ambiente
1. Criar o Banco de Dados no MySQL
Abra seu terminal ou ferramenta de gerenciamento (como DBeaver ou MySQL Workbench) e crie a base de dados:

CREATE DATABASE locadrive;

spring.datasource.url=jdbc:mysql://localhost:3306/locadrive?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO_MYSQL
spring.datasource.password=SUA_SENHA_MYSQL

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuração da Chave do Stripe (Segura via Variável de Ambiente)
stripe.api.secretKey=${STRIPE_SECRET_KEY:sk_test_sua_chave_aqui}

Como Executar o Projetos
Clone o repositório:


git clone https://github.com/antonio-samuel/locadora-backend.git
cd locadora-backend

Compilar e baixar dependências Maven:
./mvnw clean install

Executar a aplicação:

./mvnw spring-boot:run
(No Windows, você também pode usar mvnw spring-boot:run ou executar diretamente pela sua IDE).

O servidor iniciará por padrão em: http://localhost:8081

🔒 Destaques Arquiteturais & Regras de Negócio
Segurança contra SQL Injection: Implementação nativa de Prepared Statements via Spring Data JPA, blindando o banco contra entradas maliciosas.

Criptografia de Credenciais: As senhas dos usuários nunca são armazenadas em texto plano, utilizando o algoritmo robusto BCrypt.

Integridade Relacional: Tratamento de exceções personalizadas para violações de chave estrangeira (ex: impedindo a exclusão física de veículos que possuem histórico de locações ativas).

Validação de Unicidade: Restrições atômicas no backend para evitar duplicação de cadastros sensíveis como CPF, CNH e E-mail.
