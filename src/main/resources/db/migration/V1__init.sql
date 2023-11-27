CREATE TABLE idioma (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    tag TEXT NOT NULL,
    desativado BOOL NOT NULL DEFAULT FALSE
);

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    idioma INTEGER NOT NULL DEFAULT 1, -- Default PT-BR
    username TEXT NOT NULL UNIQUE,
    nome TEXT NOT NULL,
    cpf TEXT NOT NULL UNIQUE,
    telefone TEXT,
    email TEXT NOT NULL UNIQUE,
    senha TEXT NOT NULL,
    perfil TEXT,
    desativado BOOL NOT NULL DEFAULT FALSE
);

CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    tag TEXT NOT NULL,
    idioma INTEGER NOT NULL DEFAULT 1, -- Default PT-BR
    desativado BOOL NOT NULL DEFAULT FALSE
);

CREATE TABLE filme (
    id SERIAL PRIMARY KEY,
    titulo TEXT NOT NULL,
    sinopse TEXT,
    idioma INTEGER NOT NULL DEFAULT 1, -- Default PT-BR
    categoria INTEGER NOT NULL,
    imagem BYTEA,
    data_lancamento DATE NOT NULL,
    duracao INTERVAL NOT NULL,
    desativado BOOL NOT NULL DEFAULT FALSE
);
 
ALTER TABLE usuario ADD CONSTRAINT FK_usuario_2
    FOREIGN KEY (idioma)
    REFERENCES idioma (id);
 
ALTER TABLE categoria ADD CONSTRAINT FK_categoria_2
    FOREIGN KEY (idioma)
    REFERENCES idioma (id);
 
ALTER TABLE filme ADD CONSTRAINT FK_filme_2
    FOREIGN KEY (categoria)
    REFERENCES categoria (id);
 
ALTER TABLE filme ADD CONSTRAINT FK_filme_3
    FOREIGN KEY (idioma)
    REFERENCES idioma (id);
