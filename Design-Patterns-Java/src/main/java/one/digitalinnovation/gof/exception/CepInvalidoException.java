package one.digitalinnovation.gof.exception;

/**
 * Exceção lançada quando o CEP informado não é encontrado na API do ViaCEP
 * ou é inválido.
 *
 * MELHORIA (novo): Separação clara entre erros de "cliente não encontrado"
 * e "CEP inválido", permitindo respostas HTTP mais precisas.
 */
public class CepInvalidoException extends RuntimeException {

    public CepInvalidoException(String cep) {
        super("CEP '" + cep + "' não encontrado ou inválido. Verifique o CEP informado.");
    }
}
