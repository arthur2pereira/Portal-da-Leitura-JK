import { useState } from "react";
import { useSearchParams } from "react-router-dom";
import '../assets/css/redefinirSenha.css';

export default function RedefinirSenha() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token") || "";

  const [novaSenha, setNovaSenha] = useState("");
  const [confirmaSenha, setConfirmaSenha] = useState("");
  const [msg, setMsg] = useState("");
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setErro("");

    if (!token) {
      setErro("Token inválido ou ausente.");
      return;
    }

    if (novaSenha.length < 6) {
      setErro("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (novaSenha !== confirmaSenha) {
      setErro("As senhas não coincidem.");
      return;
    }

    setLoading(true);

    try {
      const res = await fetch("http://localhost:8081/alunos/redefinir-senha", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token, novaSenha }),
      });

      if (res.ok) {
        setMsg("Senha redefinida com sucesso! Você já pode fazer login.");
        setNovaSenha("");
        setConfirmaSenha("");
      } else {
        const data = await res.json();
        setErro(data.message || "Erro ao redefinir senha.");
      }
    } catch {
      setErro("Erro de conexão. Tente novamente.");
    }

    setLoading(false);
  };

  return (
    <div className="form-container">
      <h2>Redefinir Senha</h2>
      <form onSubmit={handleSubmit} className="form-login">
        <label htmlFor="novaSenha">Nova senha</label>
        <input
          type="password"
          id="novaSenha"
          placeholder="Digite a nova senha"
          value={novaSenha}
          onChange={(e) => setNovaSenha(e.target.value)}
          required
          disabled={loading}
          minLength={6}
          autoComplete="new-password"
        />
        <label htmlFor="confirmaSenha">Confirme a nova senha</label>
        <input
          type="password"
          id="confirmaSenha"
          placeholder="Confirme a nova senha"
          value={confirmaSenha}
          onChange={(e) => setConfirmaSenha(e.target.value)}
          required
          disabled={loading}
          minLength={6}
          autoComplete="new-password"
        />
        <button type="submit" disabled={loading}>
          {loading ? "Enviando..." : "Redefinir Senha"}
        </button>
      </form>

      {msg && <p className="msg-sucesso">{msg}</p>}
      {erro && <p className="msg-erro">{erro}</p>}
    </div>
  );
}
