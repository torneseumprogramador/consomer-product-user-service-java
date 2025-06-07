# ConsumerProductUserService

Este projeto é um microserviço Java (Spring Boot) que consome mensagens de um tópico Kafka, processa os dados e envia requisições para APIs de usuários e produtos.

![Arquitetura de Microserviços](microservicos.jpg)

## Objetivo

- Consumir mensagens do Kafka contendo informações de produto e usuário.
- Criar o usuário na API de usuários.
- Criar o produto na API de produtos, associando ao usuário criado.
- Repetir o envio em caso de falha, até o sucesso ou até o limite de tentativas.

## Tecnologias
- Java 17+
- Spring Boot 3
- Spring Kafka
- Maven

## Configuração

### Variáveis de Ambiente
Você pode sobrescrever as URLs das APIs, tópico e groupId do Kafka usando variáveis de ambiente:

- `USER_API_URL` — URL da API de usuários (default: http://localhost:8080/users)
- `PRODUCT_API_URL` — URL da API de produtos (default: http://localhost:8081/products)
- `KAFKA_TOPIC` — Nome do tópico Kafka (default: product-user-topic)
- `KAFKA_GROUP_ID` — GroupId do consumidor Kafka (default: consumer-product-user-service-group)

### application.yml
As principais configurações estão em `src/main/resources/application.yml`.

### Kafka
O serviço espera um Kafka rodando em `localhost:9092` (ajuste em `application.yml` se necessário).

## Execução

1. **Instale as dependências:**
   ```sh
   mvn clean install
   ```
2. **Execute a aplicação:**
   ```sh
   mvn spring-boot:run
   # ou
   ./run.sh
   ```

## Fluxo da Aplicação

1. O consumidor Kafka lê mensagens do tópico (exemplo de payload):
   ```json
   {
     "name": "Produto XPTO do Licon",
     "description": "Teste",
     "price": 149.99,
     "quantity": 20,
     "userName": "Licon",
     "userEmail": "Licon@teste.com"
   }
   ```
2. Cria o usuário na API de usuários (`POST /users`).
3. Usa o ID retornado para criar o produto na API de produtos (`POST /products`).
4. Se qualquer etapa falhar, tenta novamente (com 10s de espera) até 100 vezes.

## Limpar mensagens do Kafka

Use o script `clear-kafka-topic.sh` para limpar todas as mensagens do tópico: