# Microservicios Spring Boot

Cuatro proyectos Maven independientes: **Eureka** (`discovery-server`), **usuarios**, **pedidos** y **API Gateway**. Stack: Java 25, Spring Boot 4.0.4, Spring Cloud 2025.1.1, Micrometer + Prometheus + trazas hacia Zipkin.

## Requisitos

- JDK 25
- Maven 3.9+ (en este repo se puede usar el Maven portable en `.tools/apache-maven-3.9.9` si lo descargaste ahí)
- Docker Desktop (opcional, para PostgreSQL, Zipkin y Prometheus)

## Infraestructura local (Docker)

```bash
docker compose up -d
```

- **PostgreSQL**: `localhost:5432`, base `appdb`, usuario `app` / contraseña `app`
- **Zipkin**: http://localhost:9411
- **Prometheus**: http://localhost:9090 (hace scrape a los Actuator en el host; los microservicios deben estar levantados en la máquina host)

## Orden de arranque (sin Docker para la app)

1. `discovery-server` — puerto **8761** (consola Eureka: http://localhost:8761)
2. `usuario-service` — **8081**
3. `pedido-service` — **8082**
4. `api-gateway` — **8080** (entrada HTTP: `/api/usuarios/**` y `/api/pedidos/**`)

## URLs de los servicios (local)

### discovery-server (Eureka) - `localhost:8761`
- Consola Eureka: `http://localhost:8761`
- Actuator:
  - `http://localhost:8761/actuator/health`
  - `http://localhost:8761/actuator/info`
  - `http://localhost:8761/actuator/metrics`
  - `http://localhost:8761/actuator/prometheus`

### usuario-service - `localhost:8081`
- API:
  - `GET http://localhost:8081/api/usuarios`
  - `GET http://localhost:8081/api/usuarios/{id}`
  - `POST http://localhost:8081/api/usuarios`
  - `PUT http://localhost:8081/api/usuarios/{id}`
  - `DELETE http://localhost:8081/api/usuarios/{id}`
- Actuator:
  - `http://localhost:8081/actuator/health`
  - `http://localhost:8081/actuator/info`
  - `http://localhost:8081/actuator/metrics`
  - `http://localhost:8081/actuator/prometheus`

### pedido-service - `localhost:8082`
- API:
  - `GET http://localhost:8082/api/pedidos`
  - `GET http://localhost:8082/api/pedidos/{id}`
  - `POST http://localhost:8082/api/pedidos`
- Actuator:
  - `http://localhost:8082/actuator/health`
  - `http://localhost:8082/actuator/info`
  - `http://localhost:8082/actuator/metrics`
  - `http://localhost:8082/actuator/prometheus`

### api-gateway - `localhost:8080`
- API (rutas):
  - Usuarios:
    - `GET http://localhost:8080/api/usuarios`
    - `GET http://localhost:8080/api/usuarios/{id}`
    - `POST http://localhost:8080/api/usuarios`
    - `PUT http://localhost:8080/api/usuarios/{id}`
    - `DELETE http://localhost:8080/api/usuarios/{id}`
  - Pedidos:
    - `GET http://localhost:8080/api/pedidos`
    - `GET http://localhost:8080/api/pedidos/{id}`
    - `POST http://localhost:8080/api/pedidos`
- Actuator:
  - `http://localhost:8080/actuator/health`
  - `http://localhost:8080/actuator/info`
  - `http://localhost:8080/actuator/metrics`
  - `http://localhost:8080/actuator/prometheus`

### Comandos Maven (ejemplo)

Desde la raíz del repo, usando Maven del sistema o `.tools`:

```bash
mvn -f discovery-server/pom.xml spring-boot:run
mvn -f usuario-service/pom.xml spring-boot:run
mvn -f pedido-service/pom.xml spring-boot:run
mvn -f api-gateway/pom.xml spring-boot:run
```

### Perfil `docker` (PostgreSQL)

Con `docker compose` levantado:

```bash
mvn -f usuario-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=docker
mvn -f pedido-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=docker
```

Variables opcionales: `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`.

### Desarrollo rápido (H2 en memoria)

Por defecto `usuario-service` y `pedido-service` usan perfil **dev** con H2; no requieren PostgreSQL.

## Pruebas

En cada proyecto:

```bash
mvn test
```

## Actuator y observabilidad

- Health: `/actuator/health`
- Prometheus: `/actuator/prometheus` (expuesto en los puertos indicados arriba)
- Trazas: configuración de Zipkin en `application.yml` (`management.zipkin.tracing.endpoint`)

## Notas

- Spring Cloud Gateway 5 usa el artefacto `spring-cloud-starter-gateway-server-webflux` (no `spring-cloud-starter-gateway`).
- Los tests de integración con **Testcontainers** no se incluyen por defecto si no hay Docker en el entorno de CI; los repositorios JPA se prueban con **H2** mediante `@DataJpaTest`.
