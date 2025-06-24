import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../authContext";
import "../assets/css/livro.css";
import { jwtDecode } from "jwt-decode";

const Livro = () => {
  const { livroId } = useParams();
  const { auth } = useAuth();
  const token = auth?.token;

  const [livro, setLivro] = useState(null);
  const [erro, setErro] = useState("");
  const [reservaMensagem, setReservaMensagem] = useState("");
  const [mostrarAvaliacao, setMostrarAvaliacao] = useState(false);
  const [nota, setNota] = useState(5);
  const [comentario, setComentario] = useState("");
  const [avaliacaoMensagem, setAvaliacaoMensagem] = useState("");
  const [avaliacoes, setAvaliacoes] = useState([]);
  const [notaMedia, setNotaMedia] = useState(null);
  const [reservaAtiva, setReservaAtiva] = useState(false);


  useEffect(() => {
    setErro("");
    setReservaMensagem("");
    setAvaliacaoMensagem("");

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
      .then((data) => {
        setLivro(data);
      })
      .catch(() => {
        setErro("Não foi possível carregar o livro.");
      });

    fetch(`http://localhost:8081/livros/avaliacoes/media/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao carregar média");
        return res.json();
      })
      .then((data) => {
        setNotaMedia(data.media ?? null);
      })
      .catch(() => {
        setNotaMedia(null);
      });

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
      .then((data) => {
        setAvaliacoes(data);
      })
      .catch(() => {
        setAvaliacoes([]);
      });

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
        .then((data) => {
          setReservaAtiva(data);
        })
        .catch(() => {
          setReservaAtiva(false);
        });
    }     
  }, [livroId, token]);

  const fazerReserva = () => {
    if (!token) return;

    const decoded = jwtDecode(token); 
    const matricula = decoded.sub;
    console.log("Matricula do token:", matricula);

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
        setReservaMensagem("Reserva realizada com sucesso!");
      } else {
        throw new Error("Erro ao fazer reserva.");
      }
    })
    .catch((err) => {
      setReservaMensagem("Erro ao fazer reserva.");
    });
  };

  const enviarAvaliacao = () => {
    if (!token) return;

    const decoded = jwtDecode(token);
    const matricula = decoded.sub;
    console.log("Matricula do token:", matricula);

    fetch("http://localhost:8081/avaliacoes/criar", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        matricula,
        livroId,
        nota,
        comentario,
      }),
    })
    .then((res) => {
      if (res.ok) {
        setAvaliacaoMensagem("Avaliação enviada com sucesso!");
        setComentario("");
        setNota(5);
        setMostrarAvaliacao(false);
      } else {
        throw new Error("Erro ao enviar avaliação.");
      }
    })
    .catch((err) => {
      setAvaliacaoMensagem("Erro ao enviar avaliação.");
    });
  };


  if (erro) return <div className="alert alert-danger text-center mt-4">{erro}</div>;
  if (!livro) return <div className="text-center mt-4">Carregando livro...</div>;

  return (
    <div className="livro-detalhes">
      <h2 className="titulo">{livro.titulo}</h2>
      <p><strong>Autor:</strong> {livro.autor}</p>
      <p><strong>Editora:</strong> {livro.editora}</p>
      <p><strong>Ano de Publicação:</strong> {livro.ano_publicacao}</p>
      <p><strong>Curso:</strong> {livro.curso}</p>
      <p><strong>Gênero:</strong> {livro.genero}</p>
      <p><strong>Quantidade total:</strong> {livro.quantidadeTotal}</p>
      <p><strong>Disponível para reserva:</strong> {livro.quantidadeDisponivel}</p>
      <p><strong>Nota média:</strong> {notaMedia !== null ? notaMedia.toFixed(2) : "Sem avaliações"}</p>
      <p><strong>Descrição:</strong> {livro.descricao}</p>

      {reservaAtiva ? (
        <div className="alert alert-warning">
          Você já possui uma reserva ativa.
        </div>
      ) : livro.quantidadeDisponivel === 0 ? (
        <div className="alert alert-danger">
          Não há exemplares disponíveis para reserva no momento.
        </div>
      ) : (
        <button className="botao-reservar" onClick={fazerReserva}>
          Reservar livro
        </button>
      )}
      {reservaMensagem && <div className="alert alert-info mt-2">{reservaMensagem}</div>}

      <div className="avaliacoes mt-4">
        <h4>Avaliações</h4>
        <button
          className="btn btn-outline-secondary mb-2"
          onClick={() => setMostrarAvaliacao((prev) => !prev)}
        >
          {mostrarAvaliacao ? "Cancelar avaliação" : "Fazer avaliação"}
        </button>

        {mostrarAvaliacao && (
          <div className="avaliacao-form">
            <label>
              Nota (1 a 5):
              <input
                type="number"
                min="1"
                max="5"
                value={nota}
                onChange={(e) => setNota(parseInt(e.target.value) || 1)}
                className="form-control"
              />
            </label>
            <label>
              Comentário:
              <textarea
                className="form-control"
                value={comentario}
                onChange={(e) => setComentario(e.target.value)}
              />
            </label>
            <button className="btn btn-success mt-2" onClick={enviarAvaliacao}>
              Enviar Avaliação
            </button>
            {avaliacaoMensagem && <div className="alert alert-info mt-2">{avaliacaoMensagem}</div>}
          </div>
        )}

        {avaliacoes.length === 0 ? (
          <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
        ) : (
          <ul className="lista-avaliacoes">
            {avaliacoes.map((av, index) => (
              <li key={index}>
                <strong>Nota:</strong> {av.nota} <br />
                <strong>Comentário:</strong> {av.comentario}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Livro;
