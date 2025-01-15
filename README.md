# Project-ParkingMeter
Este é um projeto da Fase 2 da Especialização em Arquitetura e Desenvolvimento Java da FIAP.
Uma API REST de solução de parquímetros. A aplicação foi desenvolvida em Java 17, utilizando 
Spring Boot, maven, um banco de dados H2 para testes e geração de documento através do Swagger.

## Descrição do Projeto
O objetivo desta API é fornecer um sistema que deve ser responsável por gerenciar o tempo de 
estacionamento dos veículos, calcular os valores devido ao uso dos parquímetros e armazenar essas 
informações para fins de fiscalização.

## Funcionalidades
A API permite:
- **Criar** um veículo.
- **Iniciar** um estacionamento.
- **Buscar** um veículo pela placa e obter as informações dele junto com as sessões de estacionamento.
- **Buscar** uma sessão de estacionamento pelo código autenticador único, gerado ao iniciar um estacionamento,
e obter junto às informações do veículo.
- **Atualizar** as informações de um veículo.
- **Atualizar** o tempo de um estacionamento adicionando 1 hora no término, quando possível.
- **Encerrar** automaticamente um estacionamento alterando o status dele para finalizado quando o tempo acabar.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **Banco de Dados H2**
- **Mockito** e **JUnit 5**
- **Lombok**
- **Swagger**

## Estrutura do Projeto
O projeto está organizado nas seguintes camadas:
- `domain`: Define as entidades principais do domínio.
- `dto`: Representa as entradas e saidas de dados para a API.
- `gateway`: Interfaces para interação com o banco de dados.
- `usecase`: Contém os casos de uso com a lógica de negócios.
- `usecase.exception`: Exceções customizadas utilizadas nos casos de uso.
- `infrastructure.configuration`: Configurações do Controller Exception Handler.
- `infrastructure.controller`: Controladores da API.
- `infrastructure.gateway`: Implementações das interfaces do gateway.
- `infrastructure.persistence.entity`: Representa as entidades de persistência do banco de dados.
- `infrastructure.persistence.repository`: Interfaces dos repositórios Spring Data JPA.

## Pré-requisitos
- Java 17
- Maven 3.6+
- IDE como IntelliJ IDEA ou Eclipse

## Configuração e Execução
1. **Clone o repositório**:
   ```bash
   url do repositório: https://github.com/GabiFerraz/Project-ParkingMeter
   git clone git@github.com:GabiFerraz/Project-ParkingMeter.git
   ```

2. **Instale as dependências:**
   ```bash
   mvn clean install
   ```

3. **Execute o projeto:**
   ```bash
   mvn spring-boot:run
   ```

## Uso da API
Para visualização dos dados da api no banco de dados, acessar localmente o banco H2 através do endpoint:
- **Banco H2**: http://localhost:8080/h2-console
- **Driver Class**: org.h2.Driver
- **JDBC URL**: jdbc:h2:mem:ParkingMeter
- **User Name**: gb
- **Password**:

Os endpoints desenvolvidos podem ser acessados através do Swagger:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Swagger JSON**: http://localhost:8080/v3/api-docs
- Para o funcionamento correto da aplicação, existe uma ordem nas chamadas dos endpoints. Abaixo deixo a ordem com os curls das chamadas:
- Criação de um veículo:
```json
curl --location 'localhost:8080/parkingmeter/vehicle' \
--header 'Content-Type: application/json' \
--data '{
"licensePlate": "AAA0000",
"ownerName": "Bruna"
}'
```

- Criação de um estacionamento:
```json
curl --location 'localhost:8080/parkingmeter/parking-sessions' \
--header 'Content-Type: application/json' \
--data '{
"licensePlate": "AAA0000",
"startTime": "2025-01-14T22:48:00",
"endTime": "2025-01-14T23:49:00",
"paymentMethod": "PIX"
}'
```

- Busca de um veículo pela placa com as sessões de estacionamento:
```json
curl --location 'localhost:8080/parkingmeter/vehicle/AAA0000'
```

- Busca de uma sessão de estacionamento pelo código de autenticação:
```json
curl --location 'localhost:8080/parkingmeter/parking-sessions/code/0a515ada-3427-4da9-abab-0861d4265c38'
```

- Atualização de um veículo:
```json
curl --location --request PUT 'localhost:8080/parkingmeter/vehicle/AAA0000' \
--header 'Content-Type: application/json' \
--data '{
"licensePlate": "AAA0001",
"ownerName": "Amanda"
}'
```

- Atualização do tempo de um estacionamento:
```json
curl --location --request PUT 'localhost:8080/parkingmeter/parking-sessions/extend?licensePlate=AAA0000' \
--header 'Content-Type: application/json'
```

## Testes
Para executar os testes unitários:
   ```bash
   mvn test
   ```
O projeto inclui testes unitários para os principais casos de uso, utilizando Mockito
para mockar dependências e ArgumentCaptor para verificar os valores dos parâmetros nos
métodos chamados.

## Desenvolvedores:
- Bruna Casagrande Zaramela - RM: 359536
- Gabriela de Mesquita Ferraz - RM: 358745