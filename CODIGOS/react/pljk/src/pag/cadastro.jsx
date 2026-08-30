import { useState } from "react";
import "../assets/css/cadastro.css";

function CadastroAluno() {
  const [formData, setFormData] = useState({
    nome: "",
    matricula: "",
    email: "",
    senha: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const { nome, matricula, email, senha } = formData;

    if (matricula.length !== 13 || !/^\d{13}$/.test(matricula)) {
      alert("A matrícula deve conter exatamente 13 dígitos numéricos.");
      return;
    }

    if (senha.length < 6 || senha.length > 8) {
      alert("A senha deve ter entre 6 e 8 caracteres.");
      return;
    }

    const aluno = {
      nome,
      matricula,
      email,
      senha,
      status: true,
    };

    try {
      const response = await fetch("http://localhost:8081/alunos/salvar", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(aluno),
      });

      if (response.status === 201) {
        alert("Cadastro realizado com sucesso!");
        window.location.href = "/login";
      } else if (response.status === 409) {
        const mensagem = await response.text();
        alert("Erro: " + mensagem);
      } else {
        alert("Erro ao cadastrar. Tente novamente.");
      }
    } catch (error) {
      console.error("Erro de rede:", error);
      alert("Erro de conexão com o servidor.");
    }
  };

  return (
    <div className="register-page">
      <div className="register-form-area">
        <form className="register-form" onSubmit={handleSubmit}>
          <h2>Cadastro de Aluno</h2>

          <label htmlFor="matricula">Matrícula</label>
          <input
            type="text"
            name="matricula"
            placeholder="Digite sua matrícula"
            value={formData.matricula}
            onChange={handleChange}
          />

          <label htmlFor="nome">Nome</label>
          <input
            type="text"
            name="nome"
            placeholder="Digite seu nome"
            value={formData.nome}
            onChange={handleChange}
          />

          <label htmlFor="email">Email</label>
          <input
            type="email"
            name="email"
            placeholder="Digite seu email"
            value={formData.email}
            onChange={handleChange}
          />

          <label htmlFor="senha">Senha</label>
          <input
            type="password"
            name="senha"
            placeholder="Digite sua senha"
            value={formData.senha}
            onChange={handleChange}
          />

          <button type="submit" className="register-btn">Cadastrar</button>
        </form>
      </div>

      <div className="register-logo-area">
        <img src="/imagens/logo.png" alt="Logo do projeto" />
      </div>
    </div>
  );
}

export default CadastroAluno;
