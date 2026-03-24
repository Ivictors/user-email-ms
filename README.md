# Microsserviços de Cadastro de Usuário e Envio de E-mail

Este projeto consiste em um sistema distribuído baseado na arquitetura de microsserviços, desenvolvido com **Spring Boot**. O objetivo principal é gerenciar o cadastro de usuários e realizar o envio assíncrono de e-mails utilizando mensageria.

## ⚙️ O que o projeto faz?

O sistema é dividido em dois serviços independentes que se comunicam de forma assíncrona:

1. **User Microservice (Producer):** * Responsável por receber os dados do usuário.
    * Valida se o e-mail já está cadastrado no banco de dados.
    * Salva o novo usuário no banco de dados.
    * Publica uma mensagem na fila do **RabbitMQ** com os dados do e-mail de boas-vindas a ser enviado.

2. **Email Microservice (Consumer):**
    * Fica "escutando" a fila do RabbitMQ.
    * Consome as mensagens recebidas (desserializando de JSON para objeto Java).
    * Processa o e-mail e gerencia seu status (Pendente, Enviado, Falha, Entregue) salvando o histórico no banco de dados.

### Fluxo de Comunicação
Usuário cadastrado ➡️ Salvo no Banco (User) ➡️ Publicação no RabbitMQ ➡️ Consumo pelo Serviço de E-mail ➡️ Salvo no Banco (Email)

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java
* **Framework Principal:** Spring Boot 4
* **Mensageria:** RabbitMQ (Spring AMQP)
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** MySQL
* **Serialização de Dados:** Jackson 3 (`tools.jackson`) para conversão de mensagens em JSON

---

## 🛠️ Como baixar e rodar na sua máquina

### 1. Pré-requisitos
Antes de começar, você precisará ter instalado em sua máquina:
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (Versão recomendada: 21)
* [Maven](https://maven.apache.org/) para gerenciar as dependências.
* Um servidor **MySQL** rodando localmente.
* Um servidor **RabbitMQ** rodando localmente (recomenda-se o uso via Docker).

*Comando para subir o RabbitMQ via Docker (Necessario Docker instalado):
```bash
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
```

### 2. Configurações de conexão no application properties para o mySQL e Rabbit
Dentro do application properties de cada microsserviço:
```
# server.ports=8081 - cada microsserviço tem sua porta.
# Configuração do Banco de Dados - cada microsserviço tem o seu banco de dados.
spring.datasource.url=jdbc:mysql://localhost:3306/NOME_DO_SEU_BANCO
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

# Configuração do RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
broker.queue.email.name=default.email
```
### 3. Configurando os Bancos de Dados
Suba cada conteiner com o comando:
``docker compose up``
Para executar localmente - Crie dois bancos de dados no seu MySQL, um para cada microsserviço:
```
CREATE DATABASE user_db;
CREATE DATABASE email_db;
```

### 4. Clonando o Repositório
Abra o terminal e clone este repositório:

```bash
git clone [https://github.com/Ivictors/user-email-ms.git](https://github.com/Ivictors/user-email-ms)