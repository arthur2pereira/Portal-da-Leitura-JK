	CREATE DATABASE PLJK;
	USE PLJK;

	CREATE TABLE alunos (
		matricula VARCHAR(13) PRIMARY KEY,
		nome VARCHAR(100) NOT NULL,
		email VARCHAR(100) NOT NULL UNIQUE,
		senha VARCHAR(255) NOT NULL,
		status BIT DEFAULT TRUE,
		CONSTRAINT chk_matricula_format CHECK (matricula REGEXP '^[0-9]{13}$'),
		CONSTRAINT chk_email_format_alunos CHECK (email LIKE '%@%.%')
	);

	-- Tabela para Bibliotecários
	CREATE TABLE bibliotecarios (
		bibliotecario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		nome VARCHAR(100) NOT NULL,
		email VARCHAR(100) NOT NULL UNIQUE,
		senha VARCHAR(255) NOT NULL,
		CONSTRAINT chk_email_format_bibliotecarios CHECK (email LIKE '%@%.%')
	);

	-- Tabela para Livros
	CREATE TABLE livros (
		livro_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		titulo VARCHAR(150) NOT NULL,
		autor VARCHAR(100) NOT NULL,
		genero VARCHAR(50),
		curso VARCHAR(100) DEFAULT NULL,  -- Campo 'curso' é opcional
		ano_publicacao INT CHECK (ano_publicacao > 0),
		descricao TEXT,
		quantidade INT DEFAULT 1 CHECK (quantidade >= 0)
	);

	-- Tabela para Reservas
	CREATE TABLE reservas (
		reserva_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id BIGINT,
		data_reserva DATE,
		data_vencimento DATE,
		status BIT DEFAULT TRUE,
		CONSTRAINT fk_aluno_reserva FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_reserva FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT uq_aluno_reserva UNIQUE (matricula_aluno)  -- Garante que o aluno pode fazer apenas uma reserva ativa por vez
	);

	-- Tabela para Empréstimos
	CREATE TABLE emprestimos (
		emprestimo_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id BIGINT,
		bibliotecario_id BIGINT,
		data_emprestimo DATE NOT NULL,
		data_vencimento DATE,
		data_devolucao DATE,
        renovacoes INTEGER NOT NULL,
		status VARCHAR(50),
		CONSTRAINT fk_aluno_emprestimo FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_emprestimo FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT fk_bibliotecario_emprestimo FOREIGN KEY (bibliotecario_id) REFERENCES bibliotecarios(bibliotecario_id),
		CONSTRAINT uq_aluno_emprestimo UNIQUE (matricula_aluno) 
	);

	-- Tabela para Avaliações
	CREATE TABLE avaliacoes (
		avaliacao_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id BIGINT,
		nota INT CHECK (nota >= 0 AND nota <= 5),
		comentario VARCHAR(500),
		CONSTRAINT fk_aluno_avaliacao FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_avaliacao FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT uq_aluno_livro_avaliacao UNIQUE (matricula_aluno, livro_id) -- Garante que um aluno só pode avaliar um livro uma vez
	);

	-- Tabela para Notificações
	CREATE TABLE notificacoes (
		notificacao_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		mensagem VARCHAR(300),
		tipo VARCHAR(50) NOT NULL,
		lida BOOLEAN DEFAULT FALSE,
		CONSTRAINT fk_aluno FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
	);

	CREATE TABLE penalidades (
		penalidade_id BIGINT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		tipo_penalidade VARCHAR(50) NOT NULL,
        data_aplicacao DATE,
        dias_bloqueio INTEGER,
        motivo VARCHAR(255),
		CONSTRAINT fk_aluno_penalidades FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
	);
