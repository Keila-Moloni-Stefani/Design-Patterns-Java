package one.digitalinnovation.gof.event;

import one.digitalinnovation.gof.model.Cliente;
import org.springframework.context.ApplicationEvent;

/**
 * Evento de domínio publicado sempre que uma operação relevante ocorre
 * com um {@link Cliente} (inserção, atualização ou deleção).
 *
 * MELHORIA (novo): Implementação do padrão <b>Observer</b> usando o
 * mecanismo de eventos do próprio Spring (ApplicationEvent).
 * Isso desacopla o serviço de qualquer lógica de notificação futura
 * (e-mail, auditoria, mensageria, etc.) sem modificar o service.
 */
public class ClienteEvent extends ApplicationEvent {

    public enum TipoOperacao {
        INSERIDO, ATUALIZADO, DELETADO
    }

    private final TipoOperacao tipoOperacao;
    private final Long clienteId;
    private final String clienteNome;

    public ClienteEvent(Object source, TipoOperacao tipoOperacao, Cliente cliente) {
        super(source);
        this.tipoOperacao = tipoOperacao;
        this.clienteId = cliente.getId();
        this.clienteNome = cliente.getNome();
    }

    // Construtor para deleção (cliente já não existe mais)
    public ClienteEvent(Object source, TipoOperacao tipoOperacao, Long clienteId) {
        super(source);
        this.tipoOperacao = tipoOperacao;
        this.clienteId = clienteId;
        this.clienteNome = "N/A";
    }

    public TipoOperacao getTipoOperacao() { return tipoOperacao; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
}
