import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/notificacoes.css";

export default function NotificacoesAluno() {
  const [notificacoes, setNotificacoes] = useState([]);
  const [erro, setErro] = useState("");
  const { token, user } = useAuth();

  useEffect(() => {
    if (!user?.matricula) return;

    axios
      .get(`http://localhost:8081/notificacoes/aluno/${user.matricula}`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
      .then((res) => setNotificacoes(res.data))
      .catch((err) => {
        console.error(err);
        setErro("Erro ao buscar notificações.");
      });
  }, [user?.matricula, token]);

  const marcarComoLida = (id) => {
    axios
      .put(`http://localhost:8081/notificacoes/${id}/marcar-como-lida`, {}, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
      .then((res) => {
        setNotificacoes((prev) =>
          prev.map((n) => (n.id === id ? { ...n, lida: true } : n))
        );
      })
      .catch((err) => {
        console.error("Erro ao marcar como lida:", err);
      });
  };

  if (erro) return <div className="alert alert-danger text-center mt-4">{erro}</div>;

  return (
    <div className="notificacoes-container">
      <h2>Minhas Notificações</h2>
      {notificacoes.length === 0 ? (
        <p className="sem-notificacoes">Nenhuma notificação encontrada.</p>
      ) : (
        <ul className="lista-notificacoes">
          {notificacoes.map((notificacao) => (
            <li
              key={notificacao.id}
              className={`notificacao ${notificacao.lida ? "lida" : "nao-lida"}`}
            >
              <p><strong>{notificacao.titulo}</strong></p>
              <p>{notificacao.mensagem}</p>
              {!notificacao.lida && (
                <button
                  className="btn btn-sm btn-outline-success mt-2"
                  onClick={() => marcarComoLida(notificacao.id)}
                >
                  Marcar como lida
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
