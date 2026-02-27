# 🧩 Design Patterns com Spring Framework

> Projeto desenvolvido como parte do Lab **"Design Patterns com Java: Dos Clássicos (GoF) ao Spring Framework"** da [Digital Innovation One](https://dio.me).

---

## Sobre o Projeto

API REST de gerenciamento de clientes que integra automaticamente com a API pública do **ViaCEP** para busca de endereços a partir do CEP informado. O projeto aplica na prática os principais padrões de projeto (GoF) utilizando os recursos nativos do Spring Framework.

---

## Padrões de Projeto Aplicados

| Padrão | Onde foi aplicado |
|---|---|
| **Singleton** | Beans gerenciados pelo container Spring (`@Service`, `@Repository`) garantem instância única |
| **Strategy** | Interface `ClienteService` desacopla o contrato da implementação `ClienteServiceImpl` |
| **Facade** | `ClienteRestController` abstrai toda a complexidade de H2 + ViaCEP em endpoints simples |
| **Observer** | `ClienteEvent` + `ClienteEventListener` via Spring Events — notificações desacopladas por operação |

---

## Estrutura do Projeto

```
src/main/java/one/digitalinnovation/gof/
├── Application.java
├── controller/
│   └── ClienteRestController.java        # Facade + Swagger docs
├── event/
│   ├── ClienteEvent.java                 # Observer — evento de domínio
│   └── ClienteEventListener.java         # Observer — listener (logging / extensível)
├── exception/
│   ├── CepInvalidoException.java
│   ├── ClienteNotFoundException.java
│   └── GlobalExceptionHandler.java       # Tratamento centralizado de erros
├── model/
│   ├── Cliente.java                      # @NotBlank, @Valid
│   ├── ClienteRepository.java
│   ├── Endereco.java                     # @Pattern no CEP
│   └── EnderecoRepository.java
└── service/
    ├── ClienteService.java               # Strategy — interface
    ├── ViaCepService.java                # Feign Client HTTP
    └── impl/
        └── ClienteServiceImpl.java       # Singleton + Facade + Observer publisher
```

---

## Como Executar

### Pré-requisitos
- Java 11+
- Maven (ou use o wrapper incluso)

### Rodando a aplicação
```bash
# Clone o repositório
git clone https://github.com/Keila-Moloni-Stefani/Design-Patterns-Java.git
cd seu-repositorio

# Execute com o Maven Wrapper
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

---

## Endpoints da API

| Método | Endpoint | Descrição | Status de sucesso |
|---|---|---|---|
| `GET` | `/clientes` | Lista todos os clientes | `200 OK` |
| `GET` | `/clientes/{id}` | Busca cliente por ID | `200 OK` |
| `POST` | `/clientes` | Insere novo cliente (busca CEP automaticamente) | `201 Created` |
| `PUT` | `/clientes/{id}` | Atualiza cliente existente | `200 OK` |
| `DELETE` | `/clientes/{id}` | Remove cliente | `204 No Content` |

### Exemplo de requisição `POST /clientes`

```json
{
  "nome": "João Silva",
  "endereco": {
    "cep": "01310100"
  }
}
```

### Resposta de sucesso `201 Created`

```json
{
  "id": 1,
  "nome": "João Silva",
  "endereco": {
    "cep": "01310100",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    ...
  }
}
```

### Resposta de erro — Cliente não encontrado `404`

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "erro": "Cliente com ID 99 não encontrado."
}
```

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 11 | Linguagem base |
| Spring Boot | 2.5.4 | Framework principal |
| Spring Data JPA | — | Persistência com H2 |
| Spring Validation | — | Validação de entrada (Bean Validation) |
| Spring Cloud OpenFeign | 2020.0.3 | Client HTTP para ViaCEP |
| SpringDoc OpenAPI | 1.5.10 | Documentação Swagger automática |
| H2 Database | — | Banco de dados em memória |

---

## Referências

- [Digital Innovation One](https://dio.me)
- [Spring Framework Docs](https://spring.io/projects/spring-framework)
- [ViaCEP API](https://viacep.com.br)
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [SpringDoc OpenAPI](https://springdoc.org)

---

## Desenvolvedor

Desenvolvido por Keila Moloni Stefani
LinkedIn: [linkedin](https://www.linkedin.com/in/keila-moloni-stefani/)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
