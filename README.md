# 🚗 LocaDrive — Backend (API REST)

O **LocaDrive Backend** é uma API RESTful desenvolvida em Java com Spring Boot, responsável pelo gerenciamento completo de uma plataforma de locação de veículos. A aplicação trata regras de negócio severas, como cálculo dinâmico de diárias, aplicação de multas por atraso (2% ao dia), autenticação, controle de permissões e segurança dos dados.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21 LTS
- **Framework:** Spring Boot 3.5
- **Segurança & Criptografia:** Spring Security & BCrypt
- **Persistência de Dados:** Spring Data JPA / Hibernate
- **Banco de Dados:** MySQL
- **Gerenciador de Dependências:** Maven
- **Utilitário:** Lombok
- **Integração de Pagamento:** Stripe API

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [MySQL Server 8.0+](https://dev.mysql.com/downloads/installer/)
- [Git](https://git-scm.com/)
- Uma IDE de sua preferência (VS Code, IntelliJ IDEA, Eclipse)

---

## ⚙️ Configuração do Ambiente

### 1. Criar o Banco de Dados no MySQL
Abra seu terminal ou cliente MySQL (Workbench, DBeaver) e crie a base de dados:

```sql
CREATE DATABASE locadrive;
Configurar o application.properties
Localize o arquivo src/main/resources/application.properties e ajuste com as credenciais do seu banco local:

spring.datasource.url=jdbc:mysql://localhost:3306/locadrive?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO_MYSQL
spring.datasource.password=SUA_SENHA_MYSQL

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Stripe (Variável de ambiente ou chave de teste)
stripe.api.secretKey=${STRIPE_SECRET_KEY:sk_test_sua_chave_aqui}

Como Executar o Projetos
Clone o repositório:

Bash
git clone [https://github.com/antonio-samuel/locadora-backend.git](https://github.com/antonio-samuel/locadora-backend.git)
cd locadora-backend
Compilar e baixar dependências Maven:

Bash
./mvnw clean install
Executar a aplicação:

Bash
./mvnw spring-boot:run

O servidor iniciará por padrão em: http://localhost:8081
