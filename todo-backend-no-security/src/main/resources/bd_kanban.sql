-- -----------------------
-- TABLA USERS
-- -----------------------
CREATE TABLE users (
    id SERIAL PRIMARY KEY,  -- ID auto-incremental
    username VARCHAR(80) NOT NULL UNIQUE,
    email VARCHAR(200) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(200),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- -----------------------
-- ENUMS PARA TASKS
-- -----------------------
CREATE TYPE task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE task_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

-- -----------------------
-- TABLA TASKS
-- -----------------------
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,  -- ID auto-incremental
    id_usuario INT REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    status task_status NOT NULL DEFAULT 'TODO',
    priority task_priority NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- -----------------------
-- DATOS DE EJEMPLO
-- -----------------------

-- Usuarios
INSERT INTO users (username, email, password_hash, full_name)
VALUES
('alice', 'alice@example.com', '$2a$10$EXAMPLEHASHALICE', 'Alicia Pérez'),
('bob', 'bob@example.com', '$2a$10$EXAMPLEHASHBOB', 'Roberto Gómez');

-- Tareas
INSERT INTO tasks (id_usuario, title, description, status, priority)
VALUES
(1, 'Comprar materiales', 'Comprar lápices y hojas', 'TODO', 'MEDIUM'),
(1, 'Preparar presentación', 'Diapositivas para la reunión', 'IN_PROGRESS', 'HIGH'),
(1, 'Revisar backlog', 'Priorizar tareas del proyecto', 'TODO', 'HIGH'),
(1, 'Actualizar documentación', 'Actualizar README y guías', 'DONE', 'LOW'),
(2, 'Enviar correo', 'Enviar email al cliente', 'TODO', 'LOW'),
(2, 'Configurar entorno', 'Instalar dependencias y herramientas', 'IN_PROGRESS', 'MEDIUM'),
(2, 'Diseñar formulario', 'Mockups y validaciones', 'TODO', 'HIGH'),
(2, 'Revisar código', 'Code review semanal', 'IN_PROGRESS', 'MEDIUM'),
(2, 'Pruebas unitarias', 'Implementar tests', 'TODO', 'HIGH'),
(2, 'Deploy staging', 'Subir a entorno de pruebas', 'DONE', 'HIGH');
