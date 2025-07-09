DELETE FROM transacciones;
DELETE FROM presupuestos;
DELETE FROM motivos_transaccion;
DELETE FROM contactos_transferencia;
DELETE FROM espacios_trabajo_usuarios;
DELETE FROM espacios_trabajo;
DELETE FROM usuarios;

-- Sentencias INSERT para la tabla 'usuarios'
INSERT INTO usuarios (id, nombre, email, foto_perfil, proveedor, id_proveedor, rol, activo, fecha_registro, fecha_ultimo_acceso) VALUES
(1, 'Juan Perez', 'juan.perez@example.com', 'https://example.com/juan.jpg', 'GOOGLE', '12345', 'ADMIN', true, '2024-01-15 10:00:00', '2024-07-08 10:00:00'),
(2, 'Ana Gomez', 'ana.gomez@example.com', 'https://example.com/ana.jpg', 'FACEBOOK', '67890', 'USER', true, '2024-02-20 11:30:00', '2024-07-07 15:00:00'),
(3, 'Carlos Ruiz', 'carlos.ruiz@example.com', 'https://example.com/carlos.jpg', 'MICROSOFT', '54321', 'USER', true, '2024-03-10 09:00:00', '2024-07-08 11:00:00');

-- Sentencias INSERT para la tabla 'espacios_trabajo'
INSERT INTO espacios_trabajo (id, nombre, saldo, usuario_admin_id) VALUES
(1, 'Proyecto Alpha', 15000.00, 1),
(2, 'Marketing Q3', 25000.50, 2),
(3, 'Desarrollo App Móvil', 5000.75, 1);

-- Sentencias INSERT para la tabla 'espacios_trabajo_usuarios'
INSERT INTO espacios_trabajo_usuarios (espacio_trabajo_id, usuario_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 1),
(3, 3);

-- Sentencias INSERT para la tabla 'contactos_transferencia'
INSERT INTO contactos_transferencia (id, nombre, id_espacio_trabajo) VALUES
(1, 'Proveedor de Hosting', 1),
(2, 'Diseñador Gráfico Freelance', 1),
(3, 'Agencia de Publicidad', 2),
(4, 'Servicio de Catering', 3);

-- Sentencias INSERT para la tabla 'motivos_transaccion'
INSERT INTO motivos_transaccion (id, motivo, id_espacio_trabajo) VALUES
(1, 'Suscripción a servicio', 1),
(2, 'Compra de software', 1),
(3, 'Campaña publicitaria', 2),
(4, 'Almuerzo de equipo', 3),
(5, 'Salario', 1),
(6, 'Venta de producto', 2);

-- Sentencias INSERT para la tabla 'presupuestos'
INSERT INTO presupuestos (id, monto, periodo_inicio, periodo_fin, umbral_alerta, espacio_trabajo_id, motivo_id) VALUES
(1, 5000.00, '2024-07-01 00:00:00', '2024-07-31 23:59:59', 4500.00, 1, 2),
(2, 10000.00, '2024-07-01 00:00:00', '2024-09-30 23:59:59', 9000.00, 2, 3),
(3, 2000.00, '2024-07-01 00:00:00', '2024-07-31 23:59:59', 1800.00, 3, NULL);

-- Sentencias INSERT para la tabla 'transacciones'
INSERT INTO transacciones (id, tipo, monto, fecha, descripcion, nombre_completo_auditoria, fecha_creacion, espacio_trabajo_id, motivo_transaccion_id, contacto_transferencia_id) VALUES
(1, 'GASTO', 200.00, '2024-07-05', 'Renovación de dominio web', 'Juan Perez', '2024-07-05 14:00:00', 1, 1, 1),
(2, 'GASTO', 1500.00, '2024-07-06', 'Diseño de logo', 'Juan Perez', '2024-07-06 10:30:00', 1, 2, 2),
(3, 'INGRESO', 5000.00, '2024-07-07', 'Pago de cliente', 'Ana Gomez', '2024-07-07 12:00:00', 2, 6, NULL),
(4, 'GASTO', 500.00, '2024-07-08', 'Publicidad en redes sociales', 'Ana Gomez', '2024-07-08 09:45:00', 2, 3, 3),
(5, 'GASTO', 150.50, '2024-07-08', 'Almuerzo con cliente', 'Carlos Ruiz', '2024-07-08 13:15:00', 3, 4, 4),
(6, 'INGRESO', 3000.00, '2024-07-09', 'Ingreso por consultoría', 'Juan Perez', '2024-07-09 11:00:00', 1, 5, NULL),
(7, 'GASTO', 75.00, '2024-07-10', 'Suscripción a herramienta de diseño', 'Ana Gomez', '2024-07-10 16:00:00', 2, 1, NULL);
