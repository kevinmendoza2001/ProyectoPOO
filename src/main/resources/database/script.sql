CREATE TYPE rol_usuario AS ENUM ('Administrador', 'Cajero', 'Reportes');
CREATE TYPE estado_mesa AS ENUM ('Disponible', 'Ocupada', 'Reservada');
CREATE TYPE estado_pedido AS ENUM ('Pendiente', 'Pagado', 'Cancelado');

CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          cedula VARCHAR(20) UNIQUE,
                          telefono VARCHAR(20),
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          rol rol_usuario NOT NULL
);

CREATE TABLE productos (
                           id SERIAL PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           categoria VARCHAR(50),
                           precio DECIMAL(8,2) NOT NULL,
                           stock INT NOT NULL DEFAULT 0,
                           disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE mesas (
                       id SERIAL PRIMARY KEY,
                       numero INT NOT NULL UNIQUE,
                       capacidad INT NOT NULL,
                       estado estado_mesa NOT NULL DEFAULT 'Disponible'
);

CREATE TABLE pedidos (
                         id SERIAL PRIMARY KEY,
                         id_mesa INT NOT NULL REFERENCES mesas(id),
                         id_usuario INT NOT NULL REFERENCES usuarios(id),
                         fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         total DECIMAL(10,2) NOT NULL DEFAULT 0,
                         estado estado_pedido NOT NULL DEFAULT 'Pendiente'
);

CREATE TABLE detalle_pedido (
                                id SERIAL PRIMARY KEY,
                                id_pedido INT NOT NULL REFERENCES pedidos(id),
                                id_producto INT NOT NULL REFERENCES productos(id),
                                cantidad INT NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL
);

CREATE TABLE configuracion (
                               id INT PRIMARY KEY DEFAULT 1,
                               nombre_empresa VARCHAR(100) NOT NULL,
                               ruc VARCHAR(20),
                               direccion VARCHAR(150),
                               telefono VARCHAR(20),
                               CONSTRAINT solo_una_fila CHECK (id = 1)
);

INSERT INTO usuarios (nombre, cedula, telefono, username, password, rol) VALUES
                                                                             ('Administrador', '1729011559', '0984943767', 'admin', 'admin123', 'Administrador'),
                                                                             ('Carlos Cajero', '1714151242', '0963997831', 'cajero', 'cajero123', 'Cajero'),
                                                                             ('Rita Reportes', '1722453245', '0987654321', 'reportes', 'reportes123', 'Reportes');

INSERT INTO configuracion (id, nombre_empresa, ruc, direccion, telefono) VALUES
    (1, 'Restobar', '1729011559001', 'Quito, Ecuador', '022345678');