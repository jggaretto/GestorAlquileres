-- ============================================================
-- Sistema Gestor Inmobiliario - Base de datos local
-- Motor: MySQL
-- Base: proyecto_abm
-- Conexion Java: jdbc:mysql://localhost:3306/proyecto_abm
-- Usuario local usado en el proyecto: root
-- ============================================================

CREATE DATABASE IF NOT EXISTS proyecto_abm
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE proyecto_abm;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS pagos;
DROP TABLE IF EXISTS contratos;
DROP TABLE IF EXISTS propiedades;
DROP TABLE IF EXISTS inquilinos;
DROP TABLE IF EXISTS propietarios;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- Tabla: usuarios
-- Guarda los usuarios que pueden iniciar sesion en el sistema.
-- La aplicacion valida username, password BCrypt y rol.
-- ============================================================
CREATE TABLE usuarios (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol      VARCHAR(30)  NOT NULL
);

-- ============================================================
-- Tabla: propietarios
-- Personas duenias de las propiedades registradas.
-- Se agrega DNI porque el modelo y el DAO del proyecto lo usan.
-- ============================================================
CREATE TABLE propietarios (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email    VARCHAR(100),
    dni      VARCHAR(20)  NOT NULL UNIQUE
);

-- ============================================================
-- Tabla: inquilinos
-- Personas que alquilan propiedades.
-- ============================================================
CREATE TABLE inquilinos (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni      VARCHAR(20)  NOT NULL UNIQUE,
    telefono VARCHAR(20)
);

-- ============================================================
-- Tabla: propiedades
-- Inmuebles disponibles u ocupados.
-- ============================================================
CREATE TABLE propiedades (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    direccion      VARCHAR(200) NOT NULL,
    tipo           VARCHAR(50)  NOT NULL,
    precio_mensual DOUBLE       NOT NULL,
    disponible     BOOLEAN      NOT NULL DEFAULT TRUE,
    id_propietario INT          NOT NULL,
    CONSTRAINT fk_propiedades_propietarios
        FOREIGN KEY (id_propietario) REFERENCES propietarios(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- Tabla: contratos
-- Vincula una propiedad con un inquilino en un periodo.
-- ============================================================
CREATE TABLE contratos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_propiedad INT    NOT NULL,
    id_inquilino INT    NOT NULL,
    fecha_inicio DATE   NOT NULL,
    fecha_fin    DATE   NOT NULL,
    monto        DOUBLE NOT NULL,
    CONSTRAINT fk_contratos_propiedades
        FOREIGN KEY (id_propiedad) REFERENCES propiedades(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_contratos_inquilinos
        FOREIGN KEY (id_inquilino) REFERENCES inquilinos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- Tabla: pagos
-- Registra cada pago mensual asociado a un contrato.
-- ============================================================
CREATE TABLE pagos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    mes         VARCHAR(20) NOT NULL,
    monto       DOUBLE      NOT NULL,
    estado      VARCHAR(20) NOT NULL,
    fecha_pago  DATE,
    id_contrato INT         NOT NULL,
    CONSTRAINT fk_pagos_contratos
        FOREIGN KEY (id_contrato) REFERENCES contratos(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_pagos_estado
        CHECK (estado IN ('Pagado', 'Pendiente'))
);

-- ============================================================
-- Datos de ejemplo
-- Usuario de prueba: admin / admin123
-- Password almacenada con BCrypt para que funcione con UsuarioDAO.
-- ============================================================
INSERT INTO usuarios (username, password, rol) VALUES
('admin', '$2a$10$IGK.jLOwYUDjaKFOyTP06Oi37gNXtnoqJDDsKhG8TWrYoR7Wi.Vo6', 'ADMIN');

INSERT INTO propietarios (nombre, apellido, telefono, email, dni) VALUES
('Carlos',  'Mendez',   '2657-401234', 'carlos.mendez@gmail.com', '20123456'),
('Laura',   'Figueroa', '2657-402345', 'lfigueroa@hotmail.com',   '22345678'),
('Roberto', 'Salas',    '2657-403456', 'roberto.salas@gmail.com', '18901234'),
('Marta',   'Olivares', '2657-404567', 'marta.olivares@yahoo.com','16543210');

INSERT INTO inquilinos (nombre, apellido, dni, telefono) VALUES
('Marcos', 'Rodriguez', '38111222', '2657-405678'),
('Sofia',  'Lopez',     '40222333', '2657-406789'),
('Valeria','Nunez',     '39456789', '2657-407890');

INSERT INTO propiedades (direccion, tipo, precio_mensual, disponible, id_propietario) VALUES
('Av. San Martin 1250', 'Departamento', 120000, TRUE,  1),
('Belgrano 430',       'Casa',         200000, FALSE, 1),
('Mitre 88 Local 3',   'Local',         90000, TRUE,  2),
('Pedernera 710',      'Departamento', 150000, FALSE, 3),
('Lavalle 215',        'Casa',         180000, TRUE,  4);

INSERT INTO contratos (id_propiedad, id_inquilino, fecha_inicio, fecha_fin, monto) VALUES
(2, 1, '2025-01-01', '2026-01-01', 200000),
(4, 2, '2025-03-01', '2026-03-01', 150000);

INSERT INTO pagos (mes, monto, estado, fecha_pago, id_contrato) VALUES
('Enero 2025',   200000, 'Pagado',    '2025-01-05', 1),
('Febrero 2025', 200000, 'Pagado',    '2025-02-03', 1),
('Marzo 2025',   200000, 'Pendiente', '2025-03-10', 1),
('Marzo 2025',   150000, 'Pagado',    '2025-03-06', 2),
('Abril 2025',   150000, 'Pendiente', '2025-04-10', 2);
