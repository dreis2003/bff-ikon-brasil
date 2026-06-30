# bff-ikon-brasil

BFF da plataforma IKO Nakamura Brasil.

## Responsabilidades

- Ser o ponto unico de comunicacao do frontend Angular.
- Validar JWT emitido pelo `ms-auth`.
- Repassar chamadas para os microsservicos internos.
- Centralizar CORS e contratos HTTP voltados para a UI.

## Portas

```text
bff-ikon-brasil:        http://localhost:8082
ms-auth:                http://localhost:8081
ms-cadastro-filiados:   http://localhost:8080
```

## Como Rodar

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Variaveis

```text
JWT_SECRET
MS_AUTH_URL
MS_CADASTRO_FILIADOS_URL
CORS_ALLOWED_ORIGINS
```

Valores default para desenvolvimento:

```text
JWT_SECRET=ikon-auth-dev-secret-change-me-ikon-auth-dev-secret-change-me
MS_AUTH_URL=http://localhost:8081
MS_CADASTRO_FILIADOS_URL=http://localhost:8080
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://127.0.0.1:4200
```

## Endpoints

Autenticacao:

```http
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
GET  /api/me

GET   /api/usuarios
POST  /api/usuarios
GET   /api/usuarios/{id}
PATCH /api/usuarios/{id}/ativar
PATCH /api/usuarios/{id}/inativar
```

Cadastro:

```http
GET   /api/filiais
POST  /api/filiais
GET   /api/filiais/{id}
PATCH /api/filiais/{id}/ativar
PATCH /api/filiais/{id}/inativar

GET   /api/filiados
POST  /api/filiados
GET   /api/filiados/{id}
PUT   /api/filiados/{id}
PATCH /api/filiados/{id}/foto-perfil
PATCH /api/filiados/{id}/ativar
PATCH /api/filiados/{id}/inativar
```

## Teste Manual

1. Suba `ms-auth`.
2. Suba `ms-cadastro-filiados`.
3. Suba `bff-ikon-brasil`.
4. Faca login em `POST http://localhost:8082/api/auth/login`.
5. Use o `accessToken` nos endpoints protegidos do BFF.

Collection Postman:

```text
docs/postman/bff-ikon-brasil.postman_collection.json
```
