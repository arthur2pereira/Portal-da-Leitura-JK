CREATE DATABASE PLJK;
USE PLJK;

ALTER DATABASE PLJK CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ALUNOS CADASTRADOS
CREATE TABLE alunos(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, -- Senha protegida (hash)
    bloqueado BOOLEAN DEFAULT FALSE -- Indica se o aluno está bloqueado
);

-- LIVROS DA BIBLIOTECA
CREATE TABLE livros(
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    editora VARCHAR(100),
    ano_publicacao YEAR,
    genero VARCHAR(100),
    quantidade_disponivel INT DEFAULT 1
);

-- CONTROLA O FLUXO DE LIVROS RETIRADOS
CREATE TABLE emprestimos(
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    livro_id INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,
    status ENUM('emprestado', 'devolvido', 'atrasado') DEFAULT 'emprestado',
    FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    FOREIGN KEY (livro_id) REFERENCES livros(id)
);

-- CONTROLA AS RESERVAS
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    livro_id INT NOT NULL,
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_limite DATE NOT NULL, -- 3 dias para retirada
    status ENUM('ativa', 'expirada', 'cancelada') DEFAULT 'ativa',
    FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    FOREIGN KEY (livro_id) REFERENCES livros(id)
);

-- CUIDA DAS AVALIAÇÕES E DOS COMENTARIOS
CREATE TABLE avaliacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    livro_id INT NOT NULL,
    nota INT CHECK (nota BETWEEN 1 AND 5),  -- Nota de 1 a 5
    comentario TEXT,  -- Comentário opcional
    data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (aluno_id) REFERENCES alunos(id) ON DELETE CASCADE,
    FOREIGN KEY (livro_id) REFERENCES livros(id) ON DELETE CASCADE
);

-- GERENCIA TODAS AS NOTIFIÇÕES 
CREATE TABLE notificacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bibliotecario_id INT NOT NULL,
    mensagem TEXT NOT NULL,
    tipo ENUM('reserva_cancelada', 'prazo_expirado', 'livro_atrasado', 'nova_reserva', 'novo_comentario'),
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (bibliotecario_id) REFERENCES alunos(id) -- Pode ser aluno ou admin
);

-- CONTROLA TODAS AS PENALIDADES
CREATE TABLE penalidades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    motivo ENUM('atraso_devolucao', 'reserva_nao_retirada', 'uso_indevido'),
    dias_punicao INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES alunos(id)
);

-- USUARIO BIBLIOTECARIO
CREATE TABLE bibliotecario(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,  -- A senha deve ser armazenada de forma segura (hash)
    tipo ENUM('admin', 'bibliotecario') DEFAULT 'bibliotecario'
);


	