import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
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
    setErro("");

    if (!emailOuMatricula || !senha) {
      setErro("Por favor, preencha todos os campos.");
      return;
    }

    const isEmail = emailOuMatricula.includes("@");
    const body = isEmail
      ? { email: emailOuMatricula.trim().toLowerCase(), senha }
      : { matricula: emailOuMatricula.trim(), senha };

    try {
      // Autenticação aluno
      const resAluno = await fetch("http://localhost:8081/alunos/autenticar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (resAluno.ok) {
        const data = await resAluno.json();

        const statusAtivo = Boolean(Number(data.user.status));
        console.log("Status convertido:", statusAtivo, typeof statusAtivo);

      if (!data.user.status) {
        const motivo = data.user.motivo;
        const dataExclusaoFinal = data.user.dataExclusaoFinal
          ? new Date(data.user.dataExclusaoFinal)
          : null;
        const hoje = new Date();

        if (motivo === "DESATIVACAO") {
          const result = await Swal.fire({
            title: "Conta Inativa",
            text: "Sua conta está desativada. Deseja reativar?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Reativar",
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#3ea66b",
            cancelButtonColor: "#005c5a",
            reverseButtons: true,
          });

          if (result.isConfirmed) {
            localStorage.setItem("matricula", data.user.matricula);
            return navigate("/reativar-conta");
          } else {
            return setErro("Conta inativa. Acesso negado.");
          }

        } else if (motivo === "EXCLUSAO") {
          if (dataExclusaoFinal && dataExclusaoFinal > hoje) {
            const result = await Swal.fire({
              title: "Conta em exclusão temporária",
              html: `Sua conta foi marcada para exclusão, mas ainda pode ser reativada até <b>${dataExclusaoFinal.toLocaleDateString("pt-BR")}</b>. Deseja reativar?`,
              icon: "warning",
              showCancelButton: true,
              confirmButtonText: "Reativar",
              cancelButtonText: "Cancelar",
              confirmButtonColor: "#3ea66b",
              cancelButtonColor: "#005c5a",
              reverseButtons: true,
            });

            if (result.isConfirmed) {
              localStorage.setItem("matricula", data.user.matricula);
              return navigate("/reativar-conta");
            } else {
              return setErro("Conta marcada para exclusão. Acesso negado.");
            }

          } else {
            await Swal.fire({
              title: "Conta Excluída",
              text: "Sua conta foi excluída permanentemente. Entre em contato com a biblioteca.",
              icon: "error",
              confirmButtonText: "OK",
              confirmButtonColor: "#005c5a",
            });
            return setErro("Conta excluída. Acesso negado.");
          }

        } else {
          return setErro("Conta inativa. Motivo não especificado.");
        }
      }

        login({
          token: data.token,
          tipo: "aluno",
          nome: data.user.nome,
          email: data.user.email,
        });
        return navigate("/");
      }

      // Autenticação bibliotecário
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

          <p className="esqueci-senha" onClick={() => navigate("/esqueci-senha")}>
            Esqueceu sua senha?
          </p>
        </form>
      </div>
    </div>
  );
}

export default Login;
