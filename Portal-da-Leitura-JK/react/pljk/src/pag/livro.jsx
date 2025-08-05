import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../authContext";
import "../assets/css/livro.css";
import {jwtDecode} from "jwt-decode";

const Livro = () => {
  const { livroId } = useParams();
  const { auth } = useAuth();
  const token = auth?.token;

  const [livro, setLivro] = useState(null);
  const [erro, setErro] = useState("");
  const [mensagemReserva, setMensagemReserva] = useState("");
  const [mostrarFormAvaliacao, setMostrarFormAvaliacao] = useState(false);
  const [notaAvaliacao, setNotaAvaliacao] = useState(5);
  const [comentarioAvaliacao, setComentarioAvaliacao] = useState("");
  const [mensagemAvaliacao, setMensagemAvaliacao] = useState("");
  const [avaliacoes, setAvaliacoes] = useState([]);
  const [mediaNota, setMediaNota] = useState(null);
  const [temReservaAtiva, setTemReservaAtiva] = useState(false);
  const [quantidadeDisponivel, setQuantidadeDisponivel] = useState(null);

  useEffect(() => {
    setErro("");
    setMensagemReserva("");
    setMensagemAvaliacao("");
    setLivro(null);
    setQuantidadeDisponivel(null);

    // Buscar dados do livro
    fetch(`http://localhost:8081/livros/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao carregar livro");
        return res.json();
      })
      .then(setLivro)
      .catch(() => setErro("Não foi possível carregar o livro."));

    // Buscar média das avaliações
    fetch(`http://localhost:8081/livros/avaliacao/media/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao carregar média");
        return res.json();
      })
      .then((data) => setMediaNota(data))
      .catch(() => setMediaNota(null));

    // Buscar avaliações
    fetch(`http://localhost:8081/livros/avaliacoes/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao buscar avaliações");
        return res.json();
      })
      .then(setAvaliacoes)
      .catch(() => setAvaliacoes([]));

    // Buscar quantidade disponível separadamente
    fetch(`http://localhost:8081/livros/quantidade-disponivel/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao buscar quantidade disponível");
        return res.json();
      })
      .then(setQuantidadeDisponivel)
      .catch(() => setQuantidadeDisponivel(0));

    // Verificar reserva ativa do aluno
    if (token) {
      const decoded = jwtDecode(token);
      const matricula = decoded.sub;

      fetch(`http://localhost:8081/alunos/${matricula}/temReservaAtiva`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          if (!res.ok) throw new Error("Erro ao verificar reserva ativa");
          return res.json();
        })
        .then(setTemReservaAtiva)
        .catch(() => setTemReservaAtiva(false));
    }
  }, [livroId, token]);

  const handleReserva = () => {
    if (!token) return;

    const matricula = jwtDecode(token).sub;

    fetch("http://localhost:8081/reservas/criar", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ matricula, livroId }),
    })
      .then((res) => {
        if (res.ok) {
          setMensagemReserva("Reserva realizada com sucesso!");
          setQuantidadeDisponivel((qtd) => (qtd !== null ? qtd - 1 : qtd));
          setTemReservaAtiva(true);

          setTimeout(() => {
          fetch(`http://localhost:8081/alunos/${matricula}/temReservaAtiva`, {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          })
            .then((res) => res.ok ? res.json() : false)
            .then(setTemReservaAtiva)
            .catch(() => {});
        }, 2000);

        } else {
          return res.text().then((text) => {
            throw new Error(text || "Erro ao fazer reserva.");
          });
        }
      })
      .catch((err) => {
        setMensagemReserva(
          err.message.includes("já possui uma reserva ativa")
            ? "Você já possui uma reserva ativa."
            : "Erro ao fazer reserva."
        );
      });
  };

  const handleEnviarAvaliacao = () => {
    if (!token) return;

    const matricula = jwtDecode(token).sub;

    fetch("http://localhost:8081/avaliacoes/criar", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        matricula,
        livroId,
        nota: notaAvaliacao,
        comentario: comentarioAvaliacao,
      }),
    })
      .then((res) => {
        if (res.ok) {
          setMensagemAvaliacao("Avaliação enviada com sucesso!");
          setComentarioAvaliacao("");
          setNotaAvaliacao(5);
          setMostrarFormAvaliacao(false);

          // Atualiza avaliações e média após envio
          fetch(`http://localhost:8081/livros/avaliacoes/${livroId}`, {
            headers: {
              ...(token && { Authorization: `Bearer ${token}` }),
              "Content-Type": "application/json",
            },
          })
            .then((res) => res.ok ? res.json() : [])
            .then(setAvaliacoes)
            .catch(() => setAvaliacoes([]));

          fetch(`http://localhost:8081/livros/avaliacao/media/${livroId}`, {
            headers: {
              ...(token && { Authorization: `Bearer ${token}` }),
              "Content-Type": "application/json",
            },
          })
            .then((res) => res.ok ? res.json() : null)
            .then(setMediaNota)
            .catch(() => setMediaNota(null));
        } else {
          throw new Error("Erro ao enviar avaliação.");
        }
      })
      .catch(() => setMensagemAvaliacao("Erro ao enviar avaliação."));
  };

  // Função para mostrar valor amigável ou padrão caso o campo esteja vazio
  const mostrarCampo = (campo, textoPadrao = "Não informado") => {
    if (campo === null || campo === undefined || campo === "") return textoPadrao;
    return campo;
  };

  if (erro)
    return (
      <div className="alert alerta-erro text-center mt-4">{erro}</div>
    );
  if (!livro)
    return (
      <div className="texto-carregando text-center mt-4">
        Carregando livro...
      </div>
    );

  return (
    <section className="detalhes-livro">
      <h2 className="titulo-livro">{mostrarCampo(livro.titulo, "Título não disponível")}</h2>

      <p><strong>Autor:</strong> {mostrarCampo(livro.autor)}</p>
      <p><strong>Editora:</strong> {mostrarCampo(livro.editora)}</p>
      <p><strong>Ano de Publicação:</strong> {mostrarCampo(livro.anoPublicacao)}</p>
      <p><strong>Curso:</strong> {mostrarCampo(livro.curso)}</p>
      <p><strong>Gênero:</strong> {mostrarCampo(livro.genero)}</p>
      <p><strong>Quantidade total:</strong> {mostrarCampo(livro.quantidade)}</p>
      <p><strong>Disponível para reserva:</strong> {quantidadeDisponivel !== null ? quantidadeDisponivel : "Carregando..."}</p>

      <p>
        <strong>Nota média:</strong>{" "}
        {mediaNota !== null ? mediaNota.toFixed(2) : "Sem avaliações"}
      </p>
      <p><strong>Descrição:</strong> {mostrarCampo(livro.descricao, "Descrição não disponível")}</p>

      {temReservaAtiva ? (
        <div className="alert alerta-aviso">
          Você já possui uma reserva ativa.
        </div>
      ) : quantidadeDisponivel === 0 ? (
        <div className="alert alerta-erro">
          Não há exemplares disponíveis para reserva no momento.
        </div>
      ) : (
        <button className="botao-reservar" onClick={handleReserva}>
          Reservar livro
        </button>
      )}
      {mensagemReserva && (
        <div className="alert alerta-info mt-2">{mensagemReserva}</div>
      )}

      <section className="avaliacoes-livro mt-4">
        <h4>Avaliações</h4>
        <button
          className="botao-toggle-avaliacao"
          onClick={() => setMostrarFormAvaliacao((prev) => !prev)}
        >
          {mostrarFormAvaliacao ? "Cancelar avaliação" : "Fazer avaliação"}
        </button>

        {mostrarFormAvaliacao && (
          <form
            className="form-avaliacao"
            onSubmit={(e) => {
              e.preventDefault();
              handleEnviarAvaliacao();
            }}
          >
            <label htmlFor="nota-avaliacao">
              Nota (1 a 5):
              <input
                id="nota-avaliacao"
                type="number"
                min="1"
                max="5"
                value={notaAvaliacao}
                onChange={(e) => setNotaAvaliacao(parseInt(e.target.value) || 1)}
              />
            </label>
            <label htmlFor="comentario-avaliacao">
              Comentário:
              <textarea
                id="comentario-avaliacao"
                value={comentarioAvaliacao}
                onChange={(e) => setComentarioAvaliacao(e.target.value)}
              />
            </label>
            <button type="submit" className="botao-enviar-avaliacao">
              Enviar Avaliação
            </button>
            {mensagemAvaliacao && (
              <div className="alert alerta-info mt-2">{mensagemAvaliacao}</div>
            )}
          </form>
        )}

        {avaliacoes.length === 0 ? (
          <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
        ) : (
          <ul className="lista-avaliacoes">
            {avaliacoes.map((av, i) => (
              <li key={i} className="avaliacao-item">
                <strong>Nota:</strong> {av.nota} <br />
                <strong>Comentário:</strong> {av.comentario}
              </li>
            ))}
          </ul>
        )}
      </section>
    </section>
  );
};

export default Livro;
