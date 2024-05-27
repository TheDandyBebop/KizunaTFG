CREATE TABLE contacto (
    id TEXT PRIMARY KEY,
    nombre TEXT,
    numero_telefono TEXT,
    clave_publica TEXT,
    imagen BLOB
);

CREATE TABLE mensaje (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contacto_id INTEGER,
    contenido_mensaje TEXT,
    FOREIGN KEY (contacto_id) REFERENCES contacto(id)
);
CREATE TABLE usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    clave_privada TEXT,
    imagen BLOB
);
