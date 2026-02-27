package one.digitalinnovation.gof.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratamento global de exceções da API.
 *
 * MELHORIA (novo): Antes, erros geravam stack traces genéricos ao cliente.
 * Agora, cada exceção retorna um JSON estruturado com timestamp, status,
 * e mensagem amigável — muito mais profissional e útil para quem consome a API.
 *
 * Padrão aplicado: semelhante ao Chain of Responsibility, onde cada
 * @ExceptionHandler trata um tipo específico de erro na cadeia.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata ClienteNotFoundException → 404 Not Found
     */
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClienteNotFound(ClienteNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Trata CepInvalidoException → 422 Unprocessable Entity
     */
    @ExceptionHandler(CepInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleCepInvalido(CepInvalidoException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    /**
     * Trata erros de validação de Bean Validation (@NotBlank, @Pattern, etc.) → 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("erro", "Erro de validação nos campos informados.");
        body.put("campos", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Trata qualquer outra exceção não mapeada → 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}
