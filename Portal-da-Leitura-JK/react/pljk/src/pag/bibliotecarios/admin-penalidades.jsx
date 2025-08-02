import { useEffect, useState } from "react";
import { UserX, CheckCircle2 } from "lucide-react";
import "../../assets/css/adminPenalidades.css";

export default function AdminPenalidades() {
  const [penalidades, setPenalidades] = useState([]);
  const [alunos, setAlunos] = useState([]);
  const [alunoId, setAlunoId] = useState("");
  const [tipo, setTipo] = useState("ATRASO");
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    buscarPenalidades();
    buscarAlunos();
  }, []);

  const buscarPenalidades = async () => {
    try {
      const resposta = await fetch("/api/penalidades");
      const dados = await resposta.json();
      setPenalidades(dados);
    } catch {
      setErro("Erro ao buscar penalidades.");
    }
  };

  const buscarAlunos = async () => {
    try {
      const resposta = await fetch("/api/alunos");
      const dados = await resposta.json();
      setAlunos(dados);
    } catch {
      setErro("Erro ao buscar alunos.");
    }
  };

  const aplicarPenalidade = async (e) => {
    e.preventDefault();
    setErro("");
    setMensagem("");

    if (!alunoId || !tipo) {
      setErro("Selecione um aluno e tipo de penalidade.");
      return;
    }

    try {
      const resposta = await fetch("/api/penalidades", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ alunoId, tipo }),
      });

      if (!resposta.ok) throw new Error();
      setMensagem("Penalidade aplicada com sucesso!");
      setAlunoId("");
      setTipo("ATRASO");
      buscarPenalidades();
    } catch {
      setErro("Erro ao aplicar penalidade.");
    }
  };

  return (
    <div className="admin-penalidades-container">
      <h2 className="admin-penalidades-title">Gerenciamento de Penalidades</h2>

      {mensagem && <div className="admin-feedback success">{mensagem}</div>}
      {erro && <div className="admin-feedback error">{erro}</div>}

      <div className="penalidade-form">
        <h4>Aplicar Penalidade</h4>
        <form onSubmit={aplicarPenalidade}>
          <select value={alunoId} onChange={(e) => setAlunoId(e.target.value)}>
            <option value="">Selecione o aluno</option>
            {alunos.map((aluno) => (
              <option key={aluno.id} value={aluno.id}>
                {aluno.nome}
              </option>
            ))}
          </select>

          <select value={tipo} onChange={(e) => setTipo(e.target.value)}>
            <option value="ATRASO">Atraso</option>
            <option value="DANO">Dano ao livro</option>
            <option value="COMPORTAMENTO">Mau comportamento</option>
          </select>

          <button type="submit" className="btn-aplicar">Aplicar</button>
        </form>
      </div>

      <div className="penalidades-tabela-wrapper">
        <h4>Penalidades Atuais</h4>
        <table className="penalidades-table">
          <thead>
            <tr>
              <th>Aluno</th>
              <th>Tipo</th>
              <th>Status</th>
              <th>Data</th>
            </tr>
          </thead>
          <tbody>
            {penalidades.map((p) => (
              <tr key={p.id}>
                <td>{p.alunoNome}</td>
                <td>{p.tipo}</td>
                <td>
                  {p.ativa ? (
                    <span className="status ativa"><UserX size={16} /> Ativa</span>
                  ) : (
                    <span className="status inativa"><CheckCircle2 size={16} /> Encerrada</span>
                  )}
                </td>
                <td>{new Date(p.dataAplicacao).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
