import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/login.css";
import { useAuth } from "../authContext";

function Login() {
  const [emailOuMatricula, setEmailOuMatricula] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const navigate = useNavigate(); 
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!emailOuMatricula || !senha) {
      setErro("Por favor, preencha todos os campos.");
      return;
    }

    const isEmail = emailOuMatricula.includes("@");
    const body = isEmail
      ? { email: emailOuMatricula.trim().toLowerCase(), senha }
      : { matricula: emailOuMatricula.trim(), senha };

    try {
      // Tenta autenticar como aluno
      const resAluno = await fetch("http://localhost:8081/alunos/autenticar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (resAluno.ok) {
        const data = await resAluno.json();
        login({
          token: data.token,
          tipo: "aluno",
          nome: data.user.nome,
          email: data.user.email,
        });
        return navigate("/");
      }

      // Se falhar, tenta autenticar como bibliotecário
      const resBib = await fetch("http://localhost:8081/bibliotecarios/autenticar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: body.email, senha }),
      });

      if (resBib.ok) {
        const data = await resBib.json();
        login({
          token: data.token,
          tipo: "bibliotecario",
          nome: data.user.nome,
          email: data.user.email,
        });
        return navigate("/admin/area");
      }

      throw new Error("Credenciais inválidas.");
    } catch (error) {
      setErro(error.message);
    }
  };

  return (
    <div className="login-page">
      <div className="login-logo-area">
        <img src="/imagens/logo.png" alt="Logo do projeto" />
      </div>

      <div className="login-form-area">
        <form className="login-form" onSubmit={handleLogin}>
          <h2>Login</h2>
          {erro && <p className="login-error">{erro}</p>}

          <label htmlFor="emailOuMatricula">E-mail ou Matrícula</label>
          <input
            type="text"
            id="emailOuMatricula"
            placeholder="Digite seu E-mail ou Matrícula"
            value={emailOuMatricula}
            onChange={(e) => setEmailOuMatricula(e.target.value)}
          />

          <label htmlFor="senha">Senha</label>
          <input
            type="password"
            id="senha"
            placeholder="Digite sua senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />

          <button type="submit" className="login-btn">Entrar</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
