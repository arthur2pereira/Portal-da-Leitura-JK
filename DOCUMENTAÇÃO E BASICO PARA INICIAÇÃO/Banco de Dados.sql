CREATE DATABASE pljk;
USE pljk;

CREATE TABLE alunos (
    matricula VARCHAR(13) NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    status BIT DEFAULT b'1',
    CONSTRAINT chk_matricula_format CHECK (matricula REGEXP '^[0-9]{13}$'),
    CONSTRAINT chk_email_format_alunos CHECK (email LIKE '%@%.%')
);

CREATE TABLE bibliotecarios (
    bibliotecario_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    CONSTRAINT chk_email_format_bibliotecarios CHECK (email LIKE '%@%.%')
);

CREATE TABLE livros (
    livro_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    genero VARCHAR(50),
    curso VARCHAR(100) DEFAULT NULL,
    ano_publicacao INT,
    descricao TEXT,
    quantidade INT DEFAULT 1,
    editora VARCHAR(255) NOT NULL,
    CONSTRAINT livros_chk_1 CHECK (ano_publicacao > 0),
    CONSTRAINT livros_chk_2 CHECK (quantidade >= 0)
);

CREATE TABLE reservas (
    reserva_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno VARCHAR(13),
    livro_id BIGINT,
    data_reserva DATE,
    data_vencimento DATE,
    status BIT DEFAULT b'1',
    CONSTRAINT uq_aluno_reserva UNIQUE (matricula_aluno),
    CONSTRAINT fk_aluno_reserva FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
    CONSTRAINT fk_livro_reserva FOREIGN KEY (livro_id) REFERENCES livros(livro_id)
);

CREATE TABLE emprestimos (
    emprestimo_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno VARCHAR(13),
    livro_id BIGINT,
    bibliotecario_id BIGINT,
    data_emprestimo DATE NOT NULL,
    data_vencimento DATE,
    data_devolucao DATE,
    renovacoes INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    CONSTRAINT uq_aluno_emprestimo UNIQUE (matricula_aluno),
    CONSTRAINT fk_aluno_emprestimo FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
    CONSTRAINT fk_livro_emprestimo FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
    CONSTRAINT fk_bibliotecario_emprestimo FOREIGN KEY (bibliotecario_id) REFERENCES bibliotecarios(bibliotecario_id)
);

CREATE TABLE avaliacoes (
    avaliacao_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno VARCHAR(13),
    livro_id BIGINT,
    nota INT,
    comentario VARCHAR(500),
    CONSTRAINT uq_aluno_livro_avaliacao UNIQUE (matricula_aluno, livro_id),
    CONSTRAINT avaliacoes_chk_1 CHECK (nota >= 0 AND nota <= 5),
    CONSTRAINT fk_aluno_avaliacao FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
    CONSTRAINT fk_livro_avaliacao FOREIGN KEY (livro_id) REFERENCES livros(livro_id)
);

CREATE TABLE notificacoes (
    notificacao_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno VARCHAR(13),
    mensagem VARCHAR(300),
    tipo VARCHAR(50) NOT NULL,
    lida TINYINT(1) DEFAULT 0,
    CONSTRAINT fk_aluno FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
);

CREATE TABLE penalidades (
    penalidade_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    matricula_aluno VARCHAR(13),
    tipo_penalidade VARCHAR(255) NOT NULL,
    data_aplicacao DATE,
    dias_bloqueio INT,
    motivo VARCHAR(255),
    CONSTRAINT fk_aluno_penalidades FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
);

CREATE TABLE contas_inativas (
    id_contas BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    data_exclusao_final DATETIME(6),
    data_solicitacao DATETIME(6) NOT NULL,
    email VARCHAR(100) NOT NULL,
    motivo ENUM('DESATIVACAO','EXCLUSAO') NOT NULL,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(13) NOT NULL,
    CONSTRAINT uq_contas_inativas_matricula UNIQUE (matricula),
    CONSTRAINT fk_contas_inativas_aluno FOREIGN KEY (matricula) REFERENCES alunos(matricula)
);

CREATE TABLE resete_senha_token (
    id_token BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    expiracao DATETIME(6),
    token VARCHAR(255),
    uso BIT NOT NULL,
    matricula VARCHAR(13),
    CONSTRAINT fk_resete_senha_aluno FOREIGN KEY (matricula) REFERENCES alunos(matricula)
);