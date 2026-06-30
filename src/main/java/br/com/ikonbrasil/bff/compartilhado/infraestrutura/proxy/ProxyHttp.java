package br.com.ikonbrasil.bff.compartilhado.infraestrutura.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

@Component
public class ProxyHttp {

    private static final List<String> HEADERS_IGNORADOS = List.of(
            HttpHeaders.HOST,
            HttpHeaders.CONTENT_LENGTH,
            HttpHeaders.TRANSFER_ENCODING,
            HttpHeaders.CONNECTION,
            HttpHeaders.COOKIE
    );

    private final RestClient restClient;

    public ProxyHttp(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public ResponseEntity<byte[]> encaminhar(
            HttpServletRequest request,
            HttpMethod metodo,
            String urlDestino,
            byte[] corpo
    ) {
        RestClient.RequestBodySpec spec = restClient.method(metodo)
                .uri(URI.create(urlDestino))
                .headers(headers -> copiarHeadersEntrada(request, headers));

        ResponseEntity<byte[]> resposta = corpo == null
                ? spec.retrieve()
                        .onStatus(HttpStatusCode::isError, (requisicao, response) -> {
                        })
                        .toEntity(byte[].class)
                : spec.body(corpo)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (requisicao, response) -> {
                        })
                        .toEntity(byte[].class);

        HttpHeaders headers = new HttpHeaders();
        resposta.getHeaders().forEach((nome, valores) -> {
            if (!deveIgnorarHeader(nome)) {
                headers.put(nome, valores);
            }
        });
        return new ResponseEntity<>(resposta.getBody(), headers, resposta.getStatusCode());
    }

    public static String montarUrl(String baseUrl, String caminho, String queryString) {
        StringBuilder url = new StringBuilder();
        url.append(baseUrl);
        if (!baseUrl.endsWith("/") && !caminho.startsWith("/")) {
            url.append("/");
        }
        url.append(caminho);
        if (queryString != null && !queryString.isBlank()) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    private static void copiarHeadersEntrada(HttpServletRequest request, HttpHeaders destino) {
        request.getHeaderNames().asIterator().forEachRemaining(nome -> {
            if (!deveIgnorarHeader(nome)) {
                destino.put(nome, java.util.Collections.list(request.getHeaders(nome)));
            }
        });
    }

    private static boolean deveIgnorarHeader(String nome) {
        return HEADERS_IGNORADOS.stream().anyMatch(header -> header.equalsIgnoreCase(nome));
    }
}
