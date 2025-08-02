import { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/notificacoes.css";

export default function Notificacoes() {
  const { user, token } = useAuth();
  const [notificacoes, setNotificacoes] = useState([]);
  const [erro, setErro] = useState("");

  useEffect(() => {
    if (user?.matricula) {
      fetch(`http://localhost:8081/notificacoes/aluno/${user.matricula}`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
        .then((res) => {
          if (!res.ok) throw new Error("Erro ao buscar notificações");
          return res.json();
        })
        .then(setNotificacoes)
        .catch((err) => setErro(err.message));
    }
  }, [user, token]);

  const marcarComoLida = async (id) => {
    try {
      const response = await fetch(`http://localhost:8081/notificacoes/${id}/marcar-como-lida`, {
        method: "PUT",
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });

      if (!response.ok) throw new Error("Erro ao marcar notificação como lida");

      setNotificacoes((prev) =>
        prev.map((n) =>
          n.id === id ? { ...n, lida: true } : n
        )
      );
    } catch (err) {
      console.error(err);
      setErro(err.message);
    }
  };

  return (
    <div className="notificacoes-page">
      <h2 className="titulo">Notificações</h2>
      {erro && <p className="erro">{erro}</p>}
      {notificacoes.length === 0 ? (
        <p className="mensagem">Nenhuma notificação recebida.</p>
      ) : (
        <ul className="lista-notificacoes">
          {notificacoes.map((notificacao) => (
            <li
              key={notificacao.id}
              className={`notificacao ${notificacao.lida ? "lida" : ""}`}
            >
              <h4>{notificacao.titulo}</h4>
              <p>{notificacao.mensagem}</p>
              {!notificacao.lida && (
                <button
                  className="btn-marcar"
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
