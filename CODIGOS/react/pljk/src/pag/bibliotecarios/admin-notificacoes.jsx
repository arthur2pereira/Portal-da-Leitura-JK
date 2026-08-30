import { useEffect, useState } from "react";
import "../../assets/css/adminNotificacoes.css";

export default function NotificacoesAdmin({ bibliotecarioId }) {
  const [notificacoes, setNotificacoes] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [carregando, setCarregando] = useState(false);

  const [mensagem, setMensagem] = useState("");
  const [tipoMensagem, setTipoMensagem] = useState(""); // "success" ou "error"

  // Formulário nova notificação
  const [novaMensagem, setNovaMensagem] = useState("");
  const [novoTipo, setNovoTipo] = useState("INFO"); // ou ALERTA, etc
  const [novasMatriculas, setNovasMatriculas] = useState(""); // string com matrículas separadas por vírgula

  // Dados para edição
  const [editandoId, setEditandoId] = useState(null);
  const [editMensagem, setEditMensagem] = useState("");
  const [editTipo, setEditTipo] = useState("INFO");

  // Buscar notificações enviadas pelo bibliotecário
  async function carregarNotificacoes(reset = false) {
    setCarregando(true);
    try {
      // Aqui seu backend não paginou listagem, se quiser paginação backend ajusta o endpoint e aqui.
      const res = await fetch(`/notificacoes/bibliotecario/${bibliotecarioId}`);
      if (!res.ok) throw new Error("Erro ao buscar notificações");
      const data = await res.json();

      if (reset) setNotificacoes(data);
      else setNotificacoes((old) => [...old, ...data]);

      setMensagem("");
    } catch (e) {
      setMensagem("Erro ao carregar notificações");
      setTipoMensagem("error");
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarNotificacoes(true);
  }, [bibliotecarioId]);

  // Criar nova notificação (para vários alunos separados por vírgula)
  async function enviarNotificacao(e) {
    e.preventDefault();
    if (!novaMensagem.trim()) {
      setMensagem("Digite a mensagem da notificação");
      setTipoMensagem("error");
      return;
    }
    if (!novasMatriculas.trim()) {
      setMensagem("Informe pelo menos uma matrícula");
      setTipoMensagem("error");
      return;
    }

    const matriculasArray = novasMatriculas.split(",").map(m => m.trim()).filter(m => m.length > 0);
    if (matriculasArray.length === 0) {
      setMensagem("Informe pelo menos uma matrícula válida");
      setTipoMensagem("error");
      return;
    }

    try {
      if (matriculasArray.length === 1) {
        // Enviar para 1 aluno
        const dto = {
          matricula: matriculasArray[0],
          bibliotecarioId,
          mensagem: novaMensagem,
          tipo: novoTipo
        };
        const res = await fetch("/notificacoes/enviar", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dto),
        });
        if (!res.ok) throw new Error("Erro ao enviar notificação");
      } else {
        // Enviar em lote
        const dtos = matriculasArray.map(m => ({
          matricula: m,
          bibliotecarioId,
          mensagem: novaMensagem,
          tipo: novoTipo
        }));
        const res = await fetch("/notificacoes/enviar/lote", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dtos),
        });
        if (!res.ok) throw new Error("Erro ao enviar notificações em lote");
      }

      setMensagem("Notificação(s) enviada(s) com sucesso!");
      setTipoMensagem("success");
      setNovaMensagem("");
      setNovasMatriculas("");
      carregarNotificacoes(true);
    } catch {
      setMensagem("Erro ao enviar notificação(s)");
      setTipoMensagem("error");
    }
  }

  // Excluir notificação
  async function excluirNotificacao(notificacaoId) {
    if (!window.confirm("Confirma a exclusão desta notificação?")) return;
    try {
      const res = await fetch(`/notificacoes/${notificacaoId}`, { method: "DELETE" });
      if (!res.ok) throw new Error("Erro ao excluir");
      setMensagem("Notificação excluída com sucesso");
      setTipoMensagem("success");
      setNotificacoes(notificacoes.filter(n => n.notificacaoId !== notificacaoId));
    } catch {
      setMensagem("Erro ao excluir notificação");
      setTipoMensagem("error");
    }
  }

  // Iniciar edição
  function iniciarEdicao(notificacao) {
    setEditandoId(notificacao.notificacaoId);
    setEditMensagem(notificacao.mensagem);
    setEditTipo(notificacao.tipo);
  }

  // Cancelar edição
  function cancelarEdicao() {
    setEditandoId(null);
    setEditMensagem("");
    setEditTipo("INFO");
  }

  // Salvar edição
  async function salvarEdicao(e) {
    e.preventDefault();
    if (!editMensagem.trim()) {
      setMensagem("Mensagem não pode ficar vazia");
      setTipoMensagem("error");
      return;
    }
    try {
      const dto = { mensagem: editMensagem, tipo: editTipo };
      const res = await fetch(`/notificacoes/${editandoId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dto),
      });
      if (!res.ok) throw new Error("Erro ao editar");
      setMensagem("Notificação atualizada com sucesso");
      setTipoMensagem("success");
      cancelarEdicao();
      carregarNotificacoes(true);
    } catch {
      setMensagem("Erro ao atualizar notificação");
      setTipoMensagem("error");
    }
  }

  // Exibir usuários destinatários
  function verUsuarios(notificacao) {
    alert(`Destinatário: ${notificacao.nome} (${notificacao.matricula})`);
  }

  return (
    <div className="admin-notificacoes-container">
      <h2 className="admin-notificacoes-title">Gerenciamento de Notificações</h2>

      {mensagem && (
        <div className={`admin-notificacoes-feedback ${tipoMensagem}`}>
          {mensagem}
        </div>
      )}

      {/* Formulário nova notificação */}
      <form className="admin-notificacoes-form" onSubmit={enviarNotificacao}>
        <textarea
          className="form-control"
          rows={3}
          placeholder="Mensagem da notificação..."
          value={novaMensagem}
          onChange={(e) => setNovaMensagem(e.target.value)}
        />
        <input
          type="text"
          className="form-control"
          placeholder="Matrículas separadas por vírgula"
          value={novasMatriculas}
          onChange={(e) => setNovasMatriculas(e.target.value)}
          style={{ marginTop: 10 }}
        />
        <select
          className="form-control"
          value={novoTipo}
          onChange={(e) => setNovoTipo(e.target.value)}
          style={{ marginTop: 10, maxWidth: 150 }}
        >
          <option value="INFO">INFO</option>
          <option value="ALERTA">ALERTA</option>
          <option value="OUTRO">OUTRO</option>
        </select>
        <button type="submit" className="btn-enviar">
          Enviar Notificação
        </button>
      </form>

      {/* Tabela notificações */}
      <div className="notificacoes-table-wrapper">
        <table className="notificacoes-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Destinatário</th>
              <th>Mensagem</th>
              <th>Tipo</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {notificacoes.map((not) => (
              <tr key={not.notificacaoId}>
                <td>{not.notificacaoId}</td>

                {/* Edição inline */}
                {editandoId === not.notificacaoId ? (
                  <>
                    <td>
                      <textarea
                        value={editMensagem}
                        onChange={e => setEditMensagem(e.target.value)}
                        rows={3}
                        style={{ width: "100%", borderRadius: 8 }}
                      />
                    </td>
                    <td>
                      <select
                        value={editTipo}
                        onChange={e => setEditTipo(e.target.value)}
                      >
                        <option value="INFO">INFO</option>
                        <option value="ALERTA">ALERTA</option>
                        <option value="OUTRO">OUTRO</option>
                      </select>
                    </td>
                    <td>{not.matricula} - {not.nome}</td>
                    <td>
                      <button className="admin-notificacoes-btn" onClick={salvarEdicao}>
                        Salvar
                      </button>
                      <button className="admin-notificacoes-btn" onClick={cancelarEdicao}>
                        Cancelar
                      </button>
                    </td>
                  </>
                ) : (
                  <>
                    <td>{not.mensagem}</td>
                    <td>{not.tipo}</td>
                    <td>{not.matricula} - {not.nome}</td>
                    <td>
                      <button className="admin-notificacoes-btn" onClick={() => iniciarEdicao(not)}>
                        Editar
                      </button>
                      <button className="admin-notificacoes-btn" onClick={() => verUsuarios(not)}>
                        Ver usuário
                      </button>
                      <button className="admin-notificacoes-btn" onClick={() => excluirNotificacao(not.notificacaoId)}>
                        Excluir
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {carregando && <p>Carregando...</p>}
    </div>
  );
}
