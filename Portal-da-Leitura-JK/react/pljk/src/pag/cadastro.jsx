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

    const { nome, matricula, email, senha} = formData;

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
      status: true
    };

    try {
      const response = await fetch("http://localhost:8081/alunos/salvar", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(aluno)
      });

      if (response.status === 201) {
        alert("Cadastro realizado com sucesso!");
        window.location.href = "/entrar"; // ou useNavigate do React Router
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
    <div className="cadastro-container">
      <div className="formulario">
        <h2>Cadastro de Aluno</h2>
        <form onSubmit={handleSubmit}>
          <label>Matrícula</label>
          <input
            type="text"
            name="matricula"
            placeholder="Digite sua matrícula"
            value={formData.matricula}
            onChange={handleChange}
          />

          <label>Nome</label>
          <input
            type="text"
            name="nome"
            placeholder="Digite seu nome"
            value={formData.nome}
            onChange={handleChange}
          />

          <label>Email</label>
          <input
            type="email"
            name="email"
            placeholder="Digite seu email"
            value={formData.email}
            onChange={handleChange}
          />

          <label>Senha</label>
          <input
            type="password"
            name="senha"
            placeholder="Digite sua senha"
            value={formData.senha}
            onChange={handleChange}
          />
          <button type="submit">Cadastrar</button>
        </form>
      </div>
      <div className="lado-direito">
        <img src="/imagens/logo.png" alt="Logo do projeto" className="logo" />
      </div>
    </div>
  );
}

export default CadastroAluno;
