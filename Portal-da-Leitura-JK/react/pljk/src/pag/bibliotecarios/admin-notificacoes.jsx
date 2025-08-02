import { useEffect, useState } from "react";
import "../../assets/css/adminNotificacoes.css";

export default function NotificacoesAdmin() {
  const [notificacoes, setNotificacoes] = useState([]);
  const [novaNotificacao, setNovaNotificacao] = useState("");
  const [mensagem, setMensagem] = useState("");
  const [tipoMensagem, setTipoMensagem] = useState(""); // "success" ou "error"

  useEffect(() => {
    // Simulação de fetch inicial
    setNotificacoes([
      { id: 1, titulo: "Aviso de manutenção", mensagem: "O sistema ficará fora do ar amanhã", usuarios: ["Ana", "João"] },
      { id: 2, titulo: "Nova funcionalidade", mensagem: "Agora você pode renovar livros online", usuarios: ["Carlos"] },
    ]);
  }, []);

  const enviarNotificacao = (e) => {
    e.preventDefault();
    if (novaNotificacao.trim() === "") {
      setMensagem("Escreva uma notificação antes de enviar.");
      setTipoMensagem("error");
      return;
    }

    const nova = {
      id: notificacoes.length + 1,
      titulo: "Nova Notificação",
      mensagem: novaNotificacao,
      usuarios: ["Exemplo de usuário"]
    };

    setNotificacoes([nova, ...notificacoes]);
    setNovaNotificacao("");
    setMensagem("Notificação enviada com sucesso!");
    setTipoMensagem("success");
  };

  return (
    <div className="admin-notificacoes-container">
      <h2 className="admin-notificacoes-title">Gerenciamento de Notificações</h2>

      {mensagem && (
        <div className={`admin-notificacoes-feedback ${tipoMensagem}`}>
          {mensagem}
        </div>
      )}

      <form className="admin-notificacoes-form" onSubmit={enviarNotificacao}>
        <textarea
          className="form-control"
          rows="3"
          placeholder="Escreva uma nova notificação..."
          value={novaNotificacao}
          onChange={(e) => setNovaNotificacao(e.target.value)}
        />
        <button type="submit" className="btn-enviar">
          Enviar Notificação
        </button>
      </form>

      <div className="notificacoes-table-wrapper">
        <table className="notificacoes-table">
          <thead>
            <tr>
              <th>Título</th>
              <th>Mensagem</th>
              <th>Destinatários</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {notificacoes.map((not) => (
              <tr key={not.id}>
                <td>{not.titulo}</td>
                <td>{not.mensagem}</td>
                <td>{not.usuarios.join(", ")}</td>
                <td>
                  <button className="admin-notificacoes-btn">Editar</button>
                  <button className="admin-notificacoes-btn">Ver usuários</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
