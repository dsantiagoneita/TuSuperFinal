-- =====================================================
-- SCRIPT SQL INICIAL - TUSUPER
-- Base de datos: TuSuper | Puerto servidor: 8083
-- Productos colombianos con precios en COP
-- =====================================================

-- Crear base de datos (ejecutar primero si no existe)
-- CREATE DATABASE IF NOT EXISTS TuSuper CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE TuSuper;

-- =====================================================
-- NOTA: Las tablas se crean automáticamente por JPA
-- Este script es para referencia y carga manual
-- La aplicación inicializa datos automáticamente
-- =====================================================

-- Insertar roles (si se ejecuta manualmente)
INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_USUARIO');
INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_TENDERO');
INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_ADMIN');

-- =====================================================
-- PRODUCTOS COLOMBIANOS DE PRUEBA
-- Precios en Pesos Colombianos (COP)
-- =====================================================

-- Categoría: Bebidas
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Café Juan Valdez Clásico 500g', 'Café colombiano premium 100% arábica de las montañas de Colombia', 28500.00, 50, 5, '7702032100018', 'Bebidas', 'Juan Valdez', '/images/cafe-juan-valdez.jpg', true, NOW(), NOW()),
('Chocolate Corona 500g', 'Chocolate de mesa tradicional colombiano para preparar en leche', 12800.00, 60, 5, '7702007001396', 'Bebidas', 'Corona', '/images/chocolate-corona.jpg', true, NOW(), NOW()),
('Leche Alpina Entera 1L', 'Leche entera pasteurizada y homogeneizada', 4200.00, 120, 10, '7702001148011', 'Lácteos', 'Alpina', '/images/leche-alpina.jpg', true, NOW(), NOW());

-- Categoría: Endulzantes
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Panela Doña Panela 1kg', 'Panela orgánica del Valle del Cauca, producto natural sin aditivos', 4500.00, 100, 10, '7701234567890', 'Endulzantes', 'Doña Panela', '/images/panela.jpg', true, NOW(), NOW()),
('Azúcar Manuelita 2.5kg', 'Azúcar blanca refinada de caña colombiana', 9500.00, 65, 5, '7702123456789', 'Endulzantes', 'Manuelita', '/images/azucar.jpg', true, NOW(), NOW());

-- Categoría: Granos
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Arroz Diana 5kg', 'Arroz blanco premium colombiano, grano largo y suelto', 22900.00, 80, 10, '7702535001015', 'Granos', 'Diana', '/images/arroz-diana.jpg', true, NOW(), NOW()),
('Frijol Rojo Caraota 1kg', 'Frijol rojo seleccionado, ideal para preparar frijolada', 8900.00, 55, 5, '7705678901234', 'Granos', 'Caraota', '/images/frijol.jpg', true, NOW(), NOW()),
('Pasta Doria Spaghetti 500g', 'Pasta italiana elaborada en Colombia, de sémola de trigo', 4300.00, 100, 10, '7702025101013', 'Granos', 'Doria', '/images/pasta-doria.jpg', true, NOW(), NOW());

-- Categoría: Aceites
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Aceite Gourmet 3L', 'Aceite vegetal premium, ideal para freír y cocinar', 35900.00, 40, 5, '7702004001238', 'Aceites', 'Gourmet', '/images/aceite-gourmet.jpg', true, NOW(), NOW());

-- Categoría: Lácteos
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Queso Costeño 500g', 'Queso fresco tradicional de la Costa Caribe colombiana', 15800.00, 35, 5, '7704567890123', 'Lácteos', 'Costeño', '/images/queso-costeno.jpg', true, NOW(), NOW()),
('Yogurt Alpina Griego 150g', 'Yogurt griego natural con alto contenido proteico', 3800.00, 80, 10, '7702001148295', 'Lácteos', 'Alpina', '/images/yogurt-griego.jpg', true, NOW(), NOW());

-- Categoría: Panadería
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Arepas Don Maíz 10 unidades', 'Arepas blancas precocidas listas para asar o freír', 6900.00, 70, 5, '7702847001033', 'Panadería', 'Don Maíz', '/images/arepas.jpg', true, NOW(), NOW());

-- Categoría: Dulces
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Bocadillo Veleño 12 unidades', 'Bocadillo tradicional de guayaba de Vélez, Santander', 8500.00, 45, 5, '7703456789012', 'Dulces', 'Veleño', '/images/bocadillo.jpg', true, NOW(), NOW());

-- Categoría: Licores
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Aguardiente Antioqueño 750ml', 'Aguardiente sin azúcar, el licor tradicional de Colombia', 42000.00, 30, 3, '7702918001124', 'Licores', 'Antioqueño', '/images/aguardiente.jpg', true, NOW(), NOW()),
('Ron Medellín 8 Años 750ml', 'Ron añejo colombiano de 8 años de añejamiento', 68000.00, 20, 3, '7702918002831', 'Licores', 'Ron Medellín', '/images/ron-medellin.jpg', true, NOW(), NOW());

-- Categoría: Carnes
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Chorizo Santarrosano 500g', 'Chorizo tradicional antioqueño de Santa Rosa de Osos', 18500.00, 25, 5, '7706789012345', 'Carnes', 'Santarrosano', '/images/chorizo.jpg', true, NOW(), NOW()),
('Salchichas Zenú 500g', 'Salchichas premium de cerdo y res', 12500.00, 50, 5, '7702007000115', 'Carnes', 'Zenú', '/images/salchichas.jpg', true, NOW(), NOW());

-- Categoría: Congelados
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Empanadas Congeladas x6', 'Empanadas de carne listas para freír, masa tradicional', 9800.00, 40, 5, '7707890123456', 'Congelados', 'La Especial', '/images/empanadas.jpg', true, NOW(), NOW());

-- Categoría: Snacks
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Galletas Noel Saltinas 3 tacos', 'Galletas de sal crujientes tradicionales', 5600.00, 90, 10, '7702007012262', 'Snacks', 'Noel', '/images/saltinas.jpg', true, NOW(), NOW());

-- Categoría: Salsas
INSERT IGNORE INTO productos (nombre, descripcion, precio, cantidad, cantidad_minima, codigo_barras, categoria, marca, imagen, activo, fecha_creacion, fecha_actualizacion)
VALUES 
('Salsa de Tomate Fruco 400g', 'Salsa de tomate natural, perfecta para acompañar', 6200.00, 75, 10, '7702047001010', 'Salsas', 'Fruco', '/images/salsa-fruco.jpg', true, NOW(), NOW());

-- =====================================================
-- CONSULTAS ÚTILES PARA VERIFICACIÓN
-- =====================================================
-- SELECT * FROM roles;
-- SELECT * FROM usuarios;
-- SELECT * FROM productos WHERE activo = true;
-- SELECT * FROM pedidos ORDER BY fecha_pedido DESC;
-- SELECT p.*, u.nombre as cliente FROM pedidos p JOIN usuarios u ON p.usuario_id = u.id;
