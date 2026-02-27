package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.event.ClienteEvent;
import one.digitalinnovation.gof.exception.CepInvalidoException;
import one.digitalinnovation.gof.exception.ClienteNotFoundException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}.
 * Tratada pelo Spring como <b>Singleton</b> via {@link Service}.
 * Abstrai as integrações com H2 e ViaCEP (padrão <b>Facade</b>).
 *
 * MELHORIAS aplicadas:
 * - Logging estruturado com SLF4J em cada operação
 * - Publicação de eventos via {@link ApplicationEventPublisher} (padrão Observer)
 * - Exceções semânticas {@link ClienteNotFoundException} e {@link CepInvalidoException}
 * - Métodos inserir/atualizar retornam o objeto persistido
 * - Validação de CEP nulo antes de consultar ViaCEP
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;  // MELHORIA: Observer

    @Override
    public Iterable<Cliente> buscarTodos() {
        log.debug("Buscando todos os clientes.");
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        log.debug("Buscando cliente por ID: {}", id);
        // MELHORIA: lança exceção semântica em vez de NoSuchElementException
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    public Cliente inserir(Cliente cliente) {
        log.info("Inserindo cliente: '{}'", cliente.getNome());
        Cliente salvo = salvarClienteComCep(cliente);
        // MELHORIA: publica evento Observer após inserção
        eventPublisher.publishEvent(new ClienteEvent(this, ClienteEvent.TipoOperacao.INSERIDO, salvo));
        return salvo;
    }

    @Override
    public Cliente atualizar(Long id, Cliente cliente) {
        log.info("Atualizando cliente ID: {}", id);
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isEmpty()) {
            throw new ClienteNotFoundException(id);
        }
        cliente.setId(id); // garante que o ID correto será atualizado
        Cliente atualizado = salvarClienteComCep(cliente);
        // MELHORIA: publica evento Observer após atualização
        eventPublisher.publishEvent(new ClienteEvent(this, ClienteEvent.TipoOperacao.ATUALIZADO, atualizado));
        return atualizado;
    }

    @Override
    public void deletar(Long id) {
        log.info("Deletando cliente ID: {}", id);
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        clienteRepository.deleteById(id);
        // MELHORIA: publica evento Observer após deleção
        eventPublisher.publishEvent(new ClienteEvent(this, ClienteEvent.TipoOperacao.DELETADO, id));
    }

    /**
     * Lógica de negócio: busca/persiste o endereço via CEP antes de salvar o cliente.
     * Reutiliza endereços já consultados anteriormente (cache local no H2).
     */
    private Cliente salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        log.debug("Verificando endereço para CEP: {}", cep);

        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            log.debug("CEP {} não encontrado localmente, consultando ViaCEP...", cep);
            try {
                Endereco novoEndereco = viaCepService.consultarCep(cep);
                // MELHORIA: ViaCEP retorna objeto com cep=null quando CEP não existe
                if (novoEndereco == null || novoEndereco.getCep() == null) {
                    throw new CepInvalidoException(cep);
                }
                enderecoRepository.save(novoEndereco);
                log.debug("CEP {} salvo localmente: {}", cep, novoEndereco.getLogradouro());
                return novoEndereco;
            } catch (CepInvalidoException e) {
                throw e;
            } catch (Exception e) {
                log.error("Erro ao consultar ViaCEP para o CEP {}: {}", cep, e.getMessage());
                throw new CepInvalidoException(cep);
            }
        });

        cliente.setEndereco(endereco);
        return clienteRepository.save(cliente);
    }
}
