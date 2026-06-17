-- Limpieza de tablas
DELETE FROM Tramos_Ruta;
DELETE FROM Historial_Envios;
DELETE FROM Envios;
DELETE FROM Tarifas;
DELETE FROM Usuarios_Sistema;

-- Reinicio de secuencias en SQLite (se hace limpiando sqlite_sequence)
DELETE FROM sqlite_sequence WHERE name IN ('Usuarios_Sistema', 'Tarifas', 'Historial_Envios', 'Tramos_Ruta');

-- Carga de Usuarios del Sistema (passwords por defecto '123456')
-- IDs generados serán: 1 (Juan), 2 (Tech Solutions), 3 (Carlos), 4 (Ana)
INSERT INTO Usuarios_Sistema (nombre, dni_nif, email, password_hash, rol, numero_empleado) VALUES
('Juan Pérez', '12345678A', 'juan@example.com', '123456', 'Usuario', NULL),
('Tech Solutions', 'B12345678', 'info@tech.com', '123456', 'Empresa', NULL),
('Carlos Ruiz', '87654321B', 'carlos@example.com', '123456', 'Transportista', 'T001'),
('Ana Gómez', '56781234C', 'ana@example.com', '123456', 'Operario', 'O001');

-- Carga de Tarifas Vigentes
INSERT INTO Tarifas (categoria_tamano, precio) VALUES
('Pequeño', 5.00),
('Mediano', 10.00),
('Grande', 20.00);

-- Carga de Envío 1 (Pendiente de recogida, asignado a Carlos Ruiz en tramos de ruta)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-001', '2026-06-15 08:30:00', 1, 'Juan Pérez', '12345678A', 'Particular', 'María López', '98765432X', 'Pequeño', 'Correcto', 'Recogida en domicilio', 'Calle Mayor 10, Madrid', 'Entrega a domicilio', 'Calle Gran Vía 5, Barcelona', '2026-06-17 10:00:00', NULL, 5.00, 'Envío solicitado', NULL, 0);

-- Tramos de Ruta para TRK-001 (Recogida a domicilio y transporte)
INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) VALUES
('TRK-001', 1, 'Calle Mayor 10, Madrid', 'Madrid Central Almacén', '1234-XYZ', 3);

-- Carga de Envío 2 (En Almacén, listo para expedición o revisión)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-002', '2026-06-14 12:00:00', 2, 'Tech Solutions', 'B12345678', 'Empresa', 'Particular S.A.', 'A55555555', 'Grande', 'Correcto', 'Entrega en oficina/punto de recogida', NULL, 'Entrega en oficina/punto de recogida', 'Oficina Central Correos, Oviedo', '2026-06-14 14:00:00', '2026-06-14 14:15:00', 20.00, 'En almacén', NULL, 0);

-- Historial inicial de TRK-002
INSERT INTO Historial_Envios (numero_seguimiento, fecha_hora, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) VALUES
('TRK-002', '2026-06-14 12:00:00', 'Envío solicitado', 'Oficina Remitente', 'Alta por API', 2, NULL),
('TRK-002', '2026-06-14 14:15:00', 'Recogido', 'Oficina Remitente', 'Paquete entregado en oficina', 3, '5678-ABC'),
('TRK-002', '2026-06-14 18:00:00', 'En almacén', 'Madrid Central Almacén', 'Llegada correcta a plataforma', 4, NULL);

-- Tramos de Ruta para TRK-002 (Listo para salir al siguiente destino)
INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) VALUES
('TRK-002', 1, 'Madrid Central Almacén', 'Oviedo Oficina Central', '9999-BBB', 3);
