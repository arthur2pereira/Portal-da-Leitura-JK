import React, { useState, useEffect } from "react";
import "../../assets/css/reativarConta.css";

export default function ReativacaoConta() {
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");
  const [sucesso, setSucesso] = useState(false);
  const [carregando, setCarregando] = useState(false);
  const [matricula, setMatricula] = useState("");

    useEffect(() => {
      const matriculaSalva = localStorage.getItem("matricula");
      if (matriculaSalva) {
        setMatricula(matriculaSalva);
      }
    }, []);

  const handleReativar = async (e) => {
    e.preventDefault();
    setCarregando(true);
    setMensagem("");

    if (!matricula) {
      setMensagem("Matrícula não encontrada.");
      setCarregando(false);
      return;
    }

    if (!senha) {
      setMensagem("Por favor, insira sua senha.");
      setCarregando(false);
      return;
    }

    try {
        const res = await fetch(`http://localhost:8081/alunos/reativar/${matricula}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ senha }),
        });

      if (res.ok) {
        setSucesso(true);
        setMensagem("Conta reativada com sucesso! Redirecionando...");
        localStorage.removeItem("user");
        setTimeout(() => {
          window.location.href = "/login";
        }, 2000);
      } else {
        const texto = await res.text();
        setMensagem(texto || "Erro ao reativar conta.");
      }
    } catch (err) {
      setMensagem("Erro ao conectar com o servidor.");
    }

    setCarregando(false);
  };

  return (
    <div className="reativacao-container">
      <div className="reativacao-card">
        <h1>Conta Inativa</h1>
        <p>Para acessar sua conta novamente, insira sua senha atual:</p>

        <form onSubmit={handleReativar}>
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
          <button type="submit" disabled={carregando}>
            {carregando ? "Processando..." : "Reativar Conta"}
          </button>
        </form>

        {mensagem && (
          <div className={`mensagem ${sucesso ? "sucesso" : "erro"}`}>
            {mensagem}
          </div>
        )}
      </div>
    </div>
  );
}
