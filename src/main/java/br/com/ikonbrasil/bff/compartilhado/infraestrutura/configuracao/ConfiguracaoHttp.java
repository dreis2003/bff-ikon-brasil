package br.com.ikonbrasil.bff.compartilhado.infraestrutura.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ConfiguracaoHttp {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
