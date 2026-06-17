-- Estructura de base de datos para la aplicación de Logística

DROP TABLE IF EXISTS Tramos_Ruta;
DROP TABLE IF EXISTS Historial_Envios;
DROP TABLE IF EXISTS Envios;
DROP TABLE IF EXISTS Tarifas;
DROP TABLE IF EXISTS Usuarios_Sistema;

CREATE TABLE IF NOT EXISTS Usuarios_Sistema (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    dni_nif VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('Usuario', 'Empresa', 'Transportista', 'Operario', 'Gerente')),
    numero_empleado VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS Tarifas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    categoria_tamano VARCHAR(50) UNIQUE NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Envios (
    numero_seguimiento VARCHAR(50) PRIMARY KEY,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_creador_id INTEGER,
    
    remitente_nombre VARCHAR(100) NOT NULL,
    remitente_dni_cif VARCHAR(20) NOT NULL,
    
    destinatario_tipo VARCHAR(20) CHECK (destinatario_tipo IN ('Particular', 'Empresa')),
    destinatario_nombre VARCHAR(100) NOT NULL,
    destinatario_dni_cif VARCHAR(20) NOT NULL,
    
    categoria_tamano VARCHAR(20) CHECK (categoria_tamano IN ('Pequeño', 'Mediano', 'Grande')),
    estado_paquete VARCHAR(50) DEFAULT 'Correcto' CHECK (estado_paquete IN ('Correcto', 'Dañado', 'Abierto', 'Mojado', 'Otro')),
    
    tipo_recogida VARCHAR(100) NOT NULL,
    lugar_recogida VARCHAR(255),
    
    tipo_entrega VARCHAR(100) NOT NULL,
    lugar_entrega VARCHAR(255) NOT NULL,
    
    fecha_prevista_recogida DATETIME,
    fecha_recogida DATETIME,
    
    importe_total DECIMAL(10, 2) NOT NULL,
    
    estado_actual VARCHAR(50) NOT NULL DEFAULT 'Envío solicitado',
    motivo_incidencia TEXT,
    intentos_entrega INTEGER DEFAULT 0,
    
    FOREIGN KEY (usuario_creador_id) REFERENCES Usuarios_Sistema(id)
);

CREATE TABLE IF NOT EXISTS Historial_Envios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_seguimiento VARCHAR(50) NOT NULL,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    observaciones TEXT,
    usuario_responsable_id INTEGER,
    vehiculo_asociado VARCHAR(50),
    FOREIGN KEY (numero_seguimiento) REFERENCES Envios(numero_seguimiento),
    FOREIGN KEY (usuario_responsable_id) REFERENCES Usuarios_Sistema(id)
);

CREATE TABLE IF NOT EXISTS Tramos_Ruta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_seguimiento VARCHAR(50) NOT NULL,
    orden_tramo INTEGER NOT NULL,
    origen VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    vehiculo_matricula VARCHAR(50) NOT NULL,
    transportista_id INTEGER NOT NULL,
    FOREIGN KEY (numero_seguimiento) REFERENCES Envios(numero_seguimiento),
    FOREIGN KEY (transportista_id) REFERENCES Usuarios_Sistema(id)
);
