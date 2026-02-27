package one.digitalinnovation.gof.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller REST que representa a <b>Facade</b> da aplicação, abstraindo
 * a complexidade das integrações (H2 + ViaCEP) em uma interface simples.
 *
 * MELHORIAS aplicadas:
 * - Status HTTP corretos: 201 (Created) no POST, 204 (No Content) no DELETE
 * - @Valid nas entradas para acionar as validações do Bean Validation
 * - Documentação OpenAPI/Swagger via anotações @Operation e @ApiResponses
 * - Tratamento de erros delegado ao {@link one.digitalinnovation.gof.exception.GlobalExceptionHandler}
 */
@RestController
@RequestMapping("clientes")
@Tag(name = "Clientes", description = "API de gerenciamento de clientes com busca de CEP automática.")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
    public ResponseEntity<Iterable<Cliente>> buscarTodos() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        // MELHORIA: ClienteNotFoundException → 404 tratado pelo GlobalExceptionHandler
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Inserir novo cliente",
               description = "Cria um cliente e busca automaticamente o endereço pelo CEP informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos."),
        @ApiResponse(responseCode = "422", description = "CEP não encontrado.")
    })
    public ResponseEntity<Cliente> inserir(@RequestBody @Valid Cliente cliente) {
        // MELHORIA: retorna 201 Created (antes retornava 200) + objeto com ID gerado
        Cliente salvo = clienteService.inserir(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
        @ApiResponse(responseCode = "422", description = "CEP não encontrado.")
    })
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody @Valid Cliente cliente) {
        Cliente atualizado = clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        // MELHORIA: retorna 204 No Content (antes retornava 200 com body vazio)
        return ResponseEntity.noContent().build();
    }
}
