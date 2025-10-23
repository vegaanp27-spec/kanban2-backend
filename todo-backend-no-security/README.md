# kANBAN  Backend  — Spring Boot 3 + Java 17 + PostgreSQL + Swagger+MAVEN 

## Requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 13+

## Ejecutar
1. Crea la base de datos `kanban_2` en PostgreSQL y ajusta credenciales en `src/main/resources/application.yml`.
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
> 
> - Enums almacenados como `VARCHAR` (`EnumType.STRING`).
