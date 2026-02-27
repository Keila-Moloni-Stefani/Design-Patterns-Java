package one.digitalinnovation.gof.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener do padrão <b>Observer</b> que reage a eventos de {@link ClienteEvent}.
 *
 * MELHORIA (novo): Toda vez que um cliente é inserido, atualizado ou deletado,
 * este listener é notificado automaticamente pelo Spring sem que o service
 * precise conhecê-lo. Aqui fazemos apenas logging, mas este é o ponto ideal
 * para adicionar integrações futuras (e-mail, Kafka, auditoria em banco, etc.)
 * sem alterar nenhuma linha do ClienteServiceImpl.
 */
@Component
public class ClienteEventListener {

    private static final Logger log = LoggerFactory.getLogger(ClienteEventListener.class);

    @EventListener
    public void onClienteEvent(ClienteEvent event) {
        switch (event.getTipoOperacao()) {
            case INSERIDO:
                log.info("[OBSERVER] Cliente INSERIDO → ID: {}, Nome: '{}'",
                        event.getClienteId(), event.getClienteNome());
                // Aqui poderia: enviar e-mail de boas-vindas, publicar em fila Kafka, etc.
                break;
            case ATUALIZADO:
                log.info("[OBSERVER] Cliente ATUALIZADO → ID: {}, Nome: '{}'",
                        event.getClienteId(), event.getClienteNome());
                // Aqui poderia: registrar histórico de auditoria, invalidar cache, etc.
                break;
            case DELETADO:
                log.info("[OBSERVER] Cliente DELETADO → ID: {}", event.getClienteId());
                // Aqui poderia: limpar dados relacionados, notificar sistemas dependentes, etc.
                break;
        }
    }
}
