package one.digitalinnovation.gof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Projeto Spring Boot — Padrões de Projeto na Prática.
 *
 * Módulos utilizados:
 * - Spring Data JPA + H2 (persistência)
 * - Spring Web (API REST)
 * - Spring Validation (validação de entrada)
 * - OpenFeign (client HTTP para ViaCEP)
 * - SpringDoc OpenAPI (documentação Swagger)
 */
@EnableFeignClients
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
