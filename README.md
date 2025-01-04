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
- **Iniciar** um estacionamento.
- **Encerrar** um estacionamento.
- **Buscar** uma sessão de estacionamento.
- **Buscar** o histórico de estacionamento de um veículo pela placa.
- **Definir** a taxa de estacionamento.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Maven** para gerenciamento de dependências
- **Banco de Dados H2** para armazenamento e testes
- **Mockito** e **JUnit 5** para testes unitários
- **Lombok** para reduzir o código boilerplate
- **Swagger** para documentação da API

## Estrutura do Projeto
O projeto está organizado nas seguintes camadas:
- `domain`: Define as entidades principais do domínio.
- `gateway`: Interfaces e implementações para interação com o banco de dados.
- `usecase`: Contém os casos de uso com a lógica de negócios.
- `infrastructure.persistence.entity`: Representa as entidades de persistência do banco de dados.
- `infrastructure.persistence.repository`: Interfaces dos repositórios Spring Data JPA.
- `usecase.exception`: Exceções customizadas utilizadas nos casos de uso.

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
- **User Name**: sa
- **Password**:

Os endpoints desenvolvidos podem ser acessados através do Swagger:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- Para o funcionamento correto da aplicação, existe uma ordem nas chamadas dos endpoints. Abaixo deixo a ordem com os curls das chamadas:
- Criação de um cliente:
```json
curl --location 'localhost:8080/megabyteful/customers' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Gabi",
"cpf": 87987987912,
"phone": 84999999999999,
"email": "test@gmail.com",
"appointments": []
}'
```

- Criação de um parquímetro:

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