# Explorando Padrões de Projetos na Prática com Java + Spring

Projeto desenvolvido como parte do Lab **"Design Patterns com Java: Dos Clássicos (GoF) ao Spring Framework"** da [Digital Innovation One](https://dio.me).

---

## 🧩 Padrões de Projeto Aplicados

| Padrão | Como foi aplicado |
|---|---|
| **Singleton** | Beans Spring gerenciados pelo container (`@Service`, `@Repository`) |
| **Strategy** | Interface `ClienteService` com implementação `ClienteServiceImpl` |
| **Facade** | `ClienteRestController` abstrai H2 + ViaCEP numa API simples |
| **Observer** *(novo)* | `ClienteEvent` + `ClienteEventListener` via Spring Events |

---

## 🚀 Melhorias Implementadas

### 1. 🔍 Validação de Entrada (Bean Validation)
- `@NotBlank` no nome do cliente
- `@NotNull` + `@Valid` no endereço
- `@Pattern` no CEP (apenas 8 dígitos numéricos)

### 2. ⚠️ Tratamento Global de Exceções
- `GlobalExceptionHandler` com `@RestControllerAdvice`
- `ClienteNotFoundException` → **404 Not Found**
- `CepInvalidoException` → **422 Unprocessable Entity**
- Erros de validação → **400 Bad Request** com detalhes por campo
- Todos os erros retornam JSON estruturado com `timestamp`, `status` e `erro`

### 3. 👁️ Observer Pattern (Spring Events)
- `ClienteEvent` publicado nas operações de inserir, atualizar e deletar
- `ClienteEventListener` reage aos eventos (logging, pronto para e-mail/Kafka)
- Totalmente desacoplado do `ClienteServiceImpl`

### 4. 📡 Status HTTP Corretos
- `POST /clientes` → **201 Created** (antes: 200)
- `DELETE /clientes/{id}` → **204 No Content** (antes: 200)
- Busca por ID inexistente → **404** com mensagem clara

### 5. 📚 Documentação OpenAPI/Swagger
- Anotações `@Operation`, `@ApiResponse`, `@Tag` no controller
- Interface disponível em: `http://localhost:8080/swagger-ui.html`

### 6. 🗄️ H2 Console Habilitado
- Acesse `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clientesdb`

### 7. 📋 Logging Estruturado
- SLF4J em todas as operações do service
- Prefixo `[OBSERVER]` nos logs do listener para fácil identificação

---

## 🗂️ Estrutura do Projeto

```
src/main/java/one/digitalinnovation/gof/
├── Application.java
├── controller/
│   └── ClienteRestController.java       ← Facade + Swagger
├── event/
│   ├── ClienteEvent.java                ← Observer (evento)
│   └── ClienteEventListener.java        ← Observer (listener)
├── exception/
│   ├── CepInvalidoException.java
│   ├── ClienteNotFoundException.java
│   └── GlobalExceptionHandler.java      ← Tratamento global
├── model/
│   ├── Cliente.java                     ← @Valid, @NotBlank
│   ├── ClienteRepository.java
│   ├── Endereco.java                    ← @Pattern no CEP
│   └── EnderecoRepository.java
└── service/
    ├── ClienteService.java              ← Strategy (interface)
    ├── ViaCepService.java               ← Feign Client
    └── impl/
        └── ClienteServiceImpl.java      ← Singleton + Facade + Observer
```

---

## ▶️ Como Executar

```bash
./mvnw spring-boot:run
```

### Endpoints disponíveis:

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Busca cliente por ID |
| POST | `/clientes` | Insere novo cliente (busca CEP automaticamente) |
| PUT | `/clientes/{id}` | Atualiza cliente existente |
| DELETE | `/clientes/{id}` | Remove cliente |

### Exemplo de requisição POST:
```json
{
  "nome": "João Silva",
  "endereco": {
    "cep": "01310100"
  }
}
```

---

## 🛠️ Tecnologias

- Java 11
- Spring Boot 2.5.4
- Spring Data JPA + H2
- Spring Validation
- Spring Cloud OpenFeign
- SpringDoc OpenAPI (Swagger UI)
