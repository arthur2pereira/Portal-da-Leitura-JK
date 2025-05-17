import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/login.css";
import { useAuth } from "../authContext"

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
  
    const body = emailOuMatricula.includes('@')
      ? { email: emailOuMatricula.trim().toLowerCase(), senha }
      : { matricula: emailOuMatricula.trim(), senha };
  
    try {
      // Tenta login como aluno
      const resAluno = await fetch("http://localhost:8081/alunos/autenticar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
  
      if (resAluno.ok) {
        const data = await resAluno.json()
        login({
          token: data.token,
          tipo: "aluno",
          nome: data.nome,
          email: data.email,
        });
        return navigate("/")
      }  
  
      // Se falhou, tenta como bibliotecário
      const resBib = await fetch("http://localhost:8081/bibliotecarios/autenticar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: body.email, senha: body.senha }),
      });
  
      if (resBib.ok) {
        const data = await resBib.json()
        login({
          token: data.token,
          tipo: "bibliotecario",
          nome: data.nome,
          email: data.email
        });
        return navigate("/admin/area")
      }  
  
      throw new Error("Credenciais inválidas.");
    } catch (error) {
      setErro(error.message);
    }
  };
  
  return (
    <div className="login-container">
      <div className="lado-esquerdo">
        <img src="/imagens/logo.png" alt="Logo do projeto" className="logo" />
      </div>

      <div className="formulario">
        <h2>Login</h2>
        {erro && <p style={{ color: "red" }}>{erro}</p>}
        <form onSubmit={handleLogin}>
          <label>E-mail ou Matrícula</label>
          <input
            type="text"
            id="emailOuMatricula"
            placeholder="Digite seu E-mail ou Matrícula"
            value={emailOuMatricula}
            onChange={(e) => setEmailOuMatricula(e.target.value)}
          />

          <label>Senha</label>
          <input
            type="password"
            id="senha"
            placeholder="Digite sua senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />

          <button type="submit" className="btn-login">Entrar</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
