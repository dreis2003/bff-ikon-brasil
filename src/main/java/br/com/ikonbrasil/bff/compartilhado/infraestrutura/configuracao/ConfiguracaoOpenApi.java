package br.com.ikonbrasil.bff.compartilhado.infraestrutura.configuracao;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConfiguracaoOpenApi {

    @Bean
    OpenAPI documentacaoOpenApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("bff-ikon-brasil")
                        .description("BFF da plataforma IKO Nakamura Brasil")
                        .version("v1")
                        .contact(new Contact().name("IKO Nakamura Brasil")))
                .servers(List.of(new Server()
                        .url("http://localhost:8082")
                        .description("Ambiente local")));
    }
}
