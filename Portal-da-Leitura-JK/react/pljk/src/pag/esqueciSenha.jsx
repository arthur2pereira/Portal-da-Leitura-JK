import { useState } from "react";
import '../assets/css/esqueciSenha.css'

export default function EsqueciSenha() {
  const [email, setEmail] = useState("");
  const [msg, setMsg] = useState("");
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setErro("");
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8081/alunos/esqueci-senha", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });

      if (res.ok) {
        setMsg("Se o e-mail existir, um link para redefinir a senha foi enviado.");
      } else {
        const data = await res.json();
        setErro(data.message || "Erro ao enviar o e-mail.");
      }
    } catch {
      setErro("Erro de conexão. Tente novamente.");
    }

    setLoading(false);
  };

  return (
    <div className="form-container">
      <h2>Recuperar Senha</h2>
      <form onSubmit={handleSubmit} className="form-login">
        <label htmlFor="email">Digite seu e-mail</label>
        <input
          type="email"
          id="email"
          placeholder="seuemail@exemplo.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          disabled={loading}
        />
        <button type="submit" disabled={loading}>
          {loading ? "Enviando..." : "Enviar link"}
        </button>
      </form>

      {msg && <p className="msg-sucesso">{msg}</p>}
      {erro && <p className="msg-erro">{erro}</p>}
    </div>
  );
}
