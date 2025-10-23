# Todo Backend (sin seguridad) — Spring Boot 3 + Java 17 + PostgreSQL + Swagger

## Requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 13+

## Ejecutar
1. Crea la base de datos `todo` en PostgreSQL y ajusta credenciales en `src/main/resources/application.yml`.
2. `mvn spring-boot:run`
3. Swagger UI: http://localhost:8080/swagger-ui.html

## Endpoints (principales)
- **Usuarios**
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `POST /api/users`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`

- **Tareas**
  - `GET /api/tasks?userId={id}&status=TODO|IN_PROGRESS|DONE`
  - `POST /api/tasks`
  - `PATCH /api/tasks/{id}/status`
  - `DELETE /api/tasks/{id}`

> Notas:
> - Sin Spring Security ni JWT.
> - Enums almacenados como `VARCHAR` (`EnumType.STRING`).
