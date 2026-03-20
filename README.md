# 🚀 User-Email Microservices Ecosystem

Este é um projeto monorepo que demonstra a integração entre microserviços utilizando **Spring Boot 4.0.4** e **Java 21**. O ecossistema foca na gestão de usuários e no disparo automatizado de e-mails via mensageria.

## 🏗️ Estrutura do Projeto

O repositório está organizado em módulos Maven:
* **[User Service](./user):** Responsável pelo cadastro e gerenciamento de usuários.
* **[Email Service](./email):** Responsável pelo processamento e envio de e-mails.

## 🛠️ Tecnologias Principais
* **Java 21**
* **Spring Boot 4.0.4**
* **Spring Data JPA** (Persistência)
* **Spring AMQP (RabbitMQ)** (Mensageria)
* **MySQL** (Banco de Dados)

## 🚀 Como Executar o Projeto Completo

1. **Build Geral:**
   Na pasta raiz, execute o comando para compilar todos os módulos:
   ```bash
   mvn clean install