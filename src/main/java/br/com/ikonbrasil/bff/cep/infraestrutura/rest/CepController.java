package br.com.ikonbrasil.bff.cep.infraestrutura.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    private final RestClient restClient;

    public CepController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://viacep.com.br/ws").build();
    }

    @GetMapping("/{cep}")
    public EnderecoCepResponse buscar(@PathVariable String cep) {
        String cepNormalizado = cep == null ? "" : cep.replaceAll("\\D", "");
        if (cepNormalizado.length() != 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP deve possuir 8 digitos");
        }

        ViaCepResponse response = restClient.get()
                .uri("/{cep}/json", cepNormalizado)
                .retrieve()
                .body(ViaCepResponse.class);

        if (response == null || Boolean.TRUE.equals(response.erro())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP nao encontrado");
        }

        return new EnderecoCepResponse(
                response.logradouro(),
                null,
                response.complemento(),
                response.bairro(),
                response.localidade(),
                response.uf(),
                response.cep()
        );
    }

    public record EnderecoCepResponse(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {
    }

    private record ViaCepResponse(
            String cep,
            String logradouro,
            String complemento,
            String bairro,
            String localidade,
            String uf,
            Boolean erro
    ) {
    }
}
