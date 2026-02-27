package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente.
 * Permite múltiplas implementações (ex: mock para testes, implementação real, etc.).
 *
 * MELHORIA: Assinaturas agora retornam {@link Cliente} no inserir/atualizar,
 * permitindo ao controller retornar o objeto persistido (com ID gerado pelo banco).
 */
public interface ClienteService {

    Iterable<Cliente> buscarTodos();

    Cliente buscarPorId(Long id);

    Cliente inserir(Cliente cliente);

    Cliente atualizar(Long id, Cliente cliente);

    void deletar(Long id);

}
