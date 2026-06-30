package br.com.ikonbrasil.bff.cadastro.infraestrutura.rest;

import br.com.ikonbrasil.bff.compartilhado.infraestrutura.proxy.ProxyHttp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CadastroBffController {

    private final String msCadastroFiliadosUrl;
    private final ProxyHttp proxyHttp;

    public CadastroBffController(
            @Value("${ikon.servicos.ms-cadastro-filiados-url}") String msCadastroFiliadosUrl,
            ProxyHttp proxyHttp
    ) {
        this.msCadastroFiliadosUrl = msCadastroFiliadosUrl;
        this.proxyHttp = proxyHttp;
    }

    @GetMapping({"/filiais", "/filiais/**", "/filiados", "/filiados/**"})
    public ResponseEntity<byte[]> get(HttpServletRequest request) {
        return proxyHttp.encaminhar(request, HttpMethod.GET, urlCadastro(request), null);
    }

    @PostMapping({"/filiais", "/filiais/**", "/filiados", "/filiados/**"})
    public ResponseEntity<byte[]> post(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.POST, urlCadastro(request), corpo);
    }

    @PutMapping({"/filiais", "/filiais/**", "/filiados", "/filiados/**"})
    public ResponseEntity<byte[]> put(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.PUT, urlCadastro(request), corpo);
    }

    @PatchMapping({"/filiais", "/filiais/**", "/filiados", "/filiados/**"})
    public ResponseEntity<byte[]> patch(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.PATCH, urlCadastro(request), corpo);
    }

    @DeleteMapping({"/filiais", "/filiais/**", "/filiados", "/filiados/**"})
    public ResponseEntity<byte[]> delete(HttpServletRequest request) {
        return proxyHttp.encaminhar(request, HttpMethod.DELETE, urlCadastro(request), null);
    }

    private String urlCadastro(HttpServletRequest request) {
        String caminhoBff = request.getRequestURI().substring("/api".length());
        String caminhoDestino = "/api/v1" + caminhoBff;
        return ProxyHttp.montarUrl(msCadastroFiliadosUrl, caminhoDestino, request.getQueryString());
    }
}
