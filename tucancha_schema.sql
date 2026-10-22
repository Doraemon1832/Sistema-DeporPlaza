CREATE DATABASE IF NOT EXISTS tucancha_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tucancha_db;

-- 1. Tabla de Canchas
DROP TABLE IF EXISTS  canchas;
CREATE TABLE canchas (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    deporte VARCHAR(30) NOT NULL, -- FUTBOL, BASQUET, PADEL
    precio_por_hora INT NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    calificacion DOUBLE NOT NULL DEFAULT 5.0,
    resenas_cantidad INT NOT NULL DEFAULT 0,
    capacidad VARCHAR(50) NOT NULL,
    superficie VARCHAR(50) NOT NULL,
    iluminacion VARCHAR(50) NOT NULL,
    imagen_url VARCHAR(500) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO canchas (id, nombre, deporte, precio_por_hora, ubicacion, calificacion, resenas_cantidad, capacidad, superficie, iluminacion, imagen_url, disponible) VALUES
('1', 'Padel Club Los Pinos', 'PADEL', 110, 'La Molina, Lima', 4.7, 156, '2 vs 2', 'Césped monofilamento', 'Iluminación LED', 'https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?auto=format&fit=crop&w=800&q=80', TRUE),
('2', 'Padel Blue Center', 'PADEL', 130, 'San Borja, Lima', 4.5, 201, '2 vs 2', 'Pista panorámica', 'Iluminación LED', 'https://images.unsplash.com/photo-1595435934249-5df7ed86e1c0?auto=format&fit=crop&w=800&q=80', TRUE),
('3', 'Estadio Norte', 'FUTBOL', 120, 'San Isidro, Lima', 4.8, 234, '11 vs 11', 'Grass sintético', 'Iluminación LED', 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=800&q=80', TRUE),
('4', 'Campo Los Andes', 'FUTBOL', 80, 'Miraflores, Lima', 4.6, 189, '5 vs 5', 'Grass sintético', 'Iluminación LED', 'https://images.unsplash.com/photo-1529900748604-07564a03e7a6?auto=format&fit=crop&w=800&q=80', TRUE),
('5', 'Arena Basket Pro', 'BASQUET', 95, 'Surco, Lima', 4.9, 312, '5 vs 5', 'Parquet flotante', 'Iluminación LED', 'https://images.unsplash.com/photo-1546519638-68e109498ffc?auto=format&fit=crop&w=800&q=80', TRUE),
('6', 'Cancha Central Basket', 'BASQUET', 60, 'Jesús María, Lima', 4.3, 98, '3 vs 3', 'Cemento pulido', 'Iluminación LED', 'https://images.unsplash.com/photo-1519861531473-9200262188bf?auto=format&fit=crop&w=800&q=80', FALSE);

-- 2. Tabla de Reservas
DROP TABLE IF EXISTS reservas;
CREATE TABLE reservas (
    id VARCHAR(100) PRIMARY KEY,
    recibo_codigo VARCHAR(50) DEFAULT 'DP-0001055',
    cancha_id VARCHAR(50) NOT NULL,
    usuario_nombre VARCHAR(100) NOT NULL DEFAULT 'Alejandro Torres',
    fecha VARCHAR(20) NOT NULL,
    dia_nombre VARCHAR(20) NOT NULL,
    hora_inicio VARCHAR(10) NOT NULL,
    duracion_horas DOUBLE NOT NULL,
    precio_total INT NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL DEFAULT 'YAPE', -- YAPE, AGENTE, EFECTIVO
    codigo_operacion VARCHAR(50) DEFAULT '',
    celular VARCHAR(30) DEFAULT '',
    estado VARCHAR(30) NOT NULL DEFAULT 'CONFIRMADA', -- PENDIENTE, CONFIRMADA, CANCELADA
    creado_en BIGINT NOT NULL,
    FOREIGN KEY (cancha_id) REFERENCES canchas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO reservas (id, recibo_codigo, cancha_id, usuario_nombre, fecha, dia_nombre, hora_inicio, duracion_horas, precio_total, metodo_pago, codigo_operacion, celular, estado, creado_en) VALUES
('BK-1058', 'DP-0001058', '1', 'Alejandro Torres', '2026-10-22', 'Jue 22', '12:00', 1.0, 110, 'AGENTE', '456534', '987654321', 'CONFIRMADA', 1700000008000),
('BK-1057', 'DP-0001057', '1', 'Alejandro Torres', '2026-10-22', 'Jue 22', '12:00', 1.0, 110, 'YAPE', '654456', '987654321', 'CONFIRMADA', 1700000007000),
('BK-1056', 'DP-0001056', '1', 'Alejandro Torres', '2026-10-22', 'Jue 22', '17:00', 1.0, 110, 'AGENTE', '3425434', '987654321', 'CONFIRMADA', 1700000006000),
('BK-1055', 'DP-0001055', '1', 'Alejandro Torres', '2026-10-22', 'Jue 22', '16:00', 1.0, 110, 'EFECTIVO', '', '987654321', 'PENDIENTE', 1700000005000);

-- 3. Tabla de Favoritos
DROP TABLE IF EXISTS favoritos;
CREATE TABLE favoritos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cancha_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (cancha_id) REFERENCES canchas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabla de Usuarios
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL DEFAULT 'USUARIO' -- USUARIO, ADMINISTRADOR
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO usuarios (nombre, email, password, rol) VALUES
('Alejandro Torres', 'alejandro@email.com', '123456', 'USUARIO'),
('Administrador DeporPlaza', 'admin@deporplaza.com', '123456', 'ADMINISTRADOR');
