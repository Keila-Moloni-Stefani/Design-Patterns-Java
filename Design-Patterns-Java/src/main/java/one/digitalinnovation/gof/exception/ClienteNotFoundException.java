package one.digitalinnovation.gof.exception;

/**
 * Exceção lançada quando um Cliente não é encontrado no banco de dados.
 *
 * MELHORIA (novo): Exceção personalizada para melhor semântica de erro,
 * substituindo o comportamento padrão de NoSuchElementException.
 */
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("Cliente com ID " + id + " não encontrado.");
    }
}
