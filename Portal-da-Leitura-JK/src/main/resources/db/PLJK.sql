	CREATE DATABASE PLJK;
	USE PLJK;

	CREATE TABLE alunos (
		matricula VARCHAR(13) PRIMARY KEY,
		nome VARCHAR(100) NOT NULL,
		email VARCHAR(100) NOT NULL UNIQUE,
		senha VARCHAR(255) NOT NULL,
		data_nascimento DATE NOT NULL,
		data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		status ENUM('ativo', 'bloqueado') DEFAULT 'ativo',
		CONSTRAINT chk_matricula_format CHECK (matricula REGEXP '^[0-9]{13}$'),
		CONSTRAINT chk_email_format_alunos CHECK (email LIKE '%@%.%')
	);

	-- Tabela para Bibliotecários
	CREATE TABLE bibliotecarios (
		bibliotecario_id INT AUTO_INCREMENT PRIMARY KEY,
		nome VARCHAR(100) NOT NULL,
		email VARCHAR(100) NOT NULL UNIQUE,
		senha VARCHAR(255) NOT NULL,
		data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT chk_email_format_bibliotecarios CHECK (email LIKE '%@%.%')
	);

	-- Tabela para Livros
	CREATE TABLE livros (
		livro_id INT AUTO_INCREMENT PRIMARY KEY,
		titulo VARCHAR(255) NOT NULL,
		autor VARCHAR(255) NOT NULL,
		genero VARCHAR(50),
		curso VARCHAR(100) DEFAULT NULL,  -- Campo 'curso' é opcional
		ano_publicacao INT CHECK (ano_publicacao > 0),
		descricao TEXT,
		quantidade INT DEFAULT 1 CHECK (quantidade >= 0)
	);

	-- Tabela para Reservas
	CREATE TABLE reservas (
		reserva_id INT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id INT,
		data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		data_vencimento DATE,
		status ENUM('reservado', 'cancelado', 'devolvido') DEFAULT 'reservado',
		CONSTRAINT fk_aluno_reserva FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_reserva FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT uq_aluno_reserva UNIQUE (matricula_aluno)  -- Garante que o aluno pode fazer apenas uma reserva ativa por vez
	);

	-- Tabela para Empréstimos
	CREATE TABLE emprestimos (
		emprestimo_id INT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id INT,
		bibliotecario_id INT,
		data_emprestimo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		data_vencimento DATE,
		data_devolucao DATE,
		status ENUM('ativo', 'devolvido', 'cancelado') DEFAULT 'ativo',
		CONSTRAINT fk_aluno_emprestimo FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_emprestimo FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT fk_bibliotecario_emprestimo FOREIGN KEY (bibliotecario_id) REFERENCES bibliotecarios(bibliotecario_id)
	);

	-- Tabela para Avaliações
	CREATE TABLE avaliacoes (
		avaliacao_id INT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		livro_id INT,
		nota INT CHECK (nota >= 0 AND nota <= 5),
		comentario TEXT,
		data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_aluno_avaliacao FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula),
		CONSTRAINT fk_livro_avaliacao FOREIGN KEY (livro_id) REFERENCES livros(livro_id),
		CONSTRAINT uq_aluno_livro_avaliacao UNIQUE (matricula_aluno, livro_id) -- Garante que um aluno só pode avaliar um livro uma vez
	);

	-- Tabela para Notificações
	CREATE TABLE notificacoes (
		notificacao_id INT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		mensagem TEXT NOT NULL,
		tipo ENUM('alerta', 'penalidade', 'informativo') NOT NULL,
		lida BOOLEAN DEFAULT FALSE,
		data_notificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		CONSTRAINT fk_aluno FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
	);

	CREATE TABLE penalidades (
		penalidade_id INT AUTO_INCREMENT PRIMARY KEY,
		matricula_aluno VARCHAR(13),
		tipo_penalidade ENUM('atraso', 'nao_retirada', 'comentario_ofensivo'),
		data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		data_fim TIMESTAMP,
		CONSTRAINT fk_aluno_penalidades FOREIGN KEY (matricula_aluno) REFERENCES alunos(matricula)
	);