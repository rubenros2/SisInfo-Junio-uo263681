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

-- Carga de Envío 3 (En tránsito, desde Sevilla hacia Madrid)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-003', '2026-06-10 09:00:00', 1, 'Juan Pérez', '12345678A', 'Empresa', 'Andalucía Tech', 'B98765432', 'Mediano', 'Correcto', 'Entrega en oficina/punto de recogida', 'Sevilla Almacén', 'Entrega en oficina/punto de recogida', 'Madrid Central Almacén', '2026-06-10 11:00:00', '2026-06-10 11:30:00', 10.00, 'En tránsito', NULL, 0);

INSERT INTO Historial_Envios (numero_seguimiento, fecha_hora, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) VALUES
('TRK-003', '2026-06-10 09:00:00', 'Envío solicitado', 'Oficina Remitente', 'Alta web', 1, NULL),
('TRK-003', '2026-06-10 11:30:00', 'Recogido', 'Oficina Remitente', 'Recogida realizada', 3, '1234-XYZ'),
('TRK-003', '2026-06-10 16:00:00', 'En almacén', 'Sevilla Almacén', 'Llegada correcta', 4, NULL),
('TRK-003', '2026-06-11 08:00:00', 'En tránsito', 'Ruta Sevilla-Madrid', 'Salida de Sevilla', 4, '1234-XYZ');

INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) VALUES
('TRK-003', 1, 'Sevilla Almacén', 'Madrid Central Almacén', '1234-XYZ', 3);


-- Carga de Envío 4 (Incidencia detectada en Barcelona)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-004', '2026-06-11 10:00:00', 2, 'Tech Solutions', 'B12345678', 'Particular', 'Pedro Sánchez', '11223344C', 'Grande', 'Dañado', 'Recogida en domicilio', 'Calle Falsa 123, Madrid', 'Entrega a domicilio', 'Calle Real 45, Barcelona', '2026-06-12 10:00:00', '2026-06-12 10:15:00', 20.00, 'Incidencia detectada', 'Paquete golpeado y dañado durante descarga', 0);

INSERT INTO Historial_Envios (numero_seguimiento, fecha_hora, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) VALUES
('TRK-004', '2026-06-11 10:00:00', 'Envío solicitado', 'Madrid', 'Alta API', 2, NULL),
('TRK-004', '2026-06-12 10:15:00', 'Recogido', 'Madrid', 'Recogida en domicilio', 3, '5678-ABC'),
('TRK-004', '2026-06-12 14:00:00', 'En almacén', 'Madrid Central Almacén', 'Procesado', 4, NULL),
('TRK-004', '2026-06-13 08:00:00', 'En tránsito', 'Ruta Madrid-Barcelona', 'En camino a BCN', 4, '5678-ABC'),
('TRK-004', '2026-06-13 14:30:00', 'Incidencia detectada', 'Barcelona Hub', 'Paquete dañado en recepción', 4, NULL);

INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) VALUES
('TRK-004', 1, 'Madrid Central Almacén', 'Barcelona Hub', '5678-ABC', 3),
('TRK-004', 2, 'Barcelona Hub', 'Calle Real 45, Barcelona', '2222-DDD', 3);


-- Carga de Envío 5 (En almacén en Valencia Plataforma Logística)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-005', '2026-06-14 09:00:00', 1, 'Juan Pérez', '12345678A', 'Empresa', 'Levante Corp', 'B22334455', 'Pequeño', 'Correcto', 'Entrega en oficina/punto de recogida', 'Valencia Plataforma Logística', 'Entrega en oficina/punto de recogida', 'Bilbao Centro Distribución', '2026-06-14 12:00:00', '2026-06-14 12:30:00', 5.00, 'En almacén', NULL, 0);

INSERT INTO Historial_Envios (numero_seguimiento, fecha_hora, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) VALUES
('TRK-005', '2026-06-14 09:00:00', 'Envío solicitado', 'Valencia', 'Alta Web', 1, NULL),
('TRK-005', '2026-06-14 12:30:00', 'Recogido', 'Valencia', 'Entregado en oficina', 4, NULL),
('TRK-005', '2026-06-14 15:00:00', 'En almacén', 'Valencia Plataforma Logística', 'Esperando ruta a Bilbao', 4, NULL);

INSERT INTO Tramos_Ruta (numero_seguimiento, orden_tramo, origen, destino, vehiculo_matricula, transportista_id) VALUES
('TRK-005', 1, 'Valencia Plataforma Logística', 'Bilbao Centro Distribución', '3333-EEE', 3);


-- Carga de Envío 6 (Entregado, pasó por Bilbao)
INSERT INTO Envios (numero_seguimiento, fecha_creacion, usuario_creador_id, remitente_nombre, remitente_dni_cif, destinatario_tipo, destinatario_nombre, destinatario_dni_cif, categoria_tamano, estado_paquete, tipo_recogida, lugar_recogida, tipo_entrega, lugar_entrega, fecha_prevista_recogida, fecha_recogida, importe_total, estado_actual, motivo_incidencia, intentos_entrega) VALUES
('TRK-006', '2026-06-01 10:00:00', 2, 'Tech Solutions', 'B12345678', 'Particular', 'Luis Gómez', '55667788D', 'Mediano', 'Correcto', 'Recogida en domicilio', 'Calle Central 1, Madrid', 'Entrega a domicilio', 'Avenida Norte 10, Bilbao', '2026-06-02 10:00:00', '2026-06-02 10:30:00', 10.00, 'Entregado', NULL, 1);

INSERT INTO Historial_Envios (numero_seguimiento, fecha_hora, estado, ubicacion, observaciones, usuario_responsable_id, vehiculo_asociado) VALUES
('TRK-006', '2026-06-01 10:00:00', 'Envío solicitado', 'Madrid', 'Alta API', 2, NULL),
('TRK-006', '2026-06-02 10:30:00', 'Recogido', 'Madrid', 'Recogida a domicilio', 3, '4444-FFF'),
('TRK-006', '2026-06-02 13:00:00', 'En almacén', 'Madrid Central Almacén', 'Procesado', 4, NULL),
('TRK-006', '2026-06-03 08:00:00', 'En tránsito', 'Ruta Madrid-Bilbao', 'Salida', 4, '4444-FFF'),
('TRK-006', '2026-06-03 14:00:00', 'En almacén', 'Bilbao Centro Distribución', 'Llegada correcta', 4, NULL),
('TRK-006', '2026-06-04 09:00:00', 'En tránsito', 'Ruta reparto local', 'En reparto', 4, '7777-GGG'),
('TRK-006', '2026-06-04 11:30:00', 'Entregado', 'Domicilio Destinatario', 'Firma recibida', 3, '7777-GGG');
