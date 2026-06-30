package br.com.ikonbrasil.bff.autenticacao.infraestrutura.rest;

import br.com.ikonbrasil.bff.compartilhado.infraestrutura.proxy.ProxyHttp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthBffController {

    private final String msAuthUrl;
    private final ProxyHttp proxyHttp;

    public AuthBffController(@Value("${ikon.servicos.ms-auth-url}") String msAuthUrl, ProxyHttp proxyHttp) {
        this.msAuthUrl = msAuthUrl;
        this.proxyHttp = proxyHttp;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<byte[]> login(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.POST, url("/api/v1/auth/login", request), corpo);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<byte[]> refresh(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.POST, url("/api/v1/auth/refresh", request), corpo);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<byte[]> logout(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.POST, url("/api/v1/auth/logout", request), corpo);
    }

    @GetMapping("/me")
    public ResponseEntity<byte[]> me(HttpServletRequest request) {
        return proxyHttp.encaminhar(request, HttpMethod.GET, url("/api/v1/auth/me", request), null);
    }

    @GetMapping({"/usuarios", "/usuarios/**"})
    public ResponseEntity<byte[]> getUsuarios(HttpServletRequest request) {
        return proxyHttp.encaminhar(request, HttpMethod.GET, urlUsuarios(request), null);
    }

    @PostMapping({"/usuarios", "/usuarios/**"})
    public ResponseEntity<byte[]> postUsuarios(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.POST, urlUsuarios(request), corpo);
    }

    @PutMapping({"/usuarios", "/usuarios/**"})
    public ResponseEntity<byte[]> putUsuarios(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.PUT, urlUsuarios(request), corpo);
    }

    @PatchMapping({"/usuarios", "/usuarios/**"})
    public ResponseEntity<byte[]> patchUsuarios(HttpServletRequest request, @RequestBody(required = false) byte[] corpo) {
        return proxyHttp.encaminhar(request, HttpMethod.PATCH, urlUsuarios(request), corpo);
    }

    private String url(String caminho, HttpServletRequest request) {
        return ProxyHttp.montarUrl(msAuthUrl, caminho, request.getQueryString());
    }

    private String urlUsuarios(HttpServletRequest request) {
        String caminhoBff = request.getRequestURI().substring("/api".length());
        String caminhoDestino = "/api/v1" + caminhoBff;
        return ProxyHttp.montarUrl(msAuthUrl, caminhoDestino, request.getQueryString());
    }
}
