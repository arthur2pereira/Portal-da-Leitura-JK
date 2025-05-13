import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../authContext";
import "../assets/css/livro.css";

const Livro = () => {
  const { livroId } = useParams();
  const [livro, setLivro] = useState(null);
  const [erro, setErro] = useState("");
  const [reservaMensagem, setReservaMensagem] = useState("");
  const [mostrarAvaliacao, setMostrarAvaliacao] = useState(false);
  const [nota, setNota] = useState(5);
  const [comentario, setComentario] = useState("");
  const [avaliacaoMensagem, setAvaliacaoMensagem] = useState("");
  const { token } = useAuth();

  useEffect(() => {
    // Carregar os dados do livro
    fetch(`http://localhost:8081/livros/${livroId}`, {
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json"
      }
    })
      .then((res) => res.json())
      .then((data) => setLivro(data))
      .catch((err) => {
        console.error(err);
        setErro("Não foi possível carregar o livro.");
      });

    // Carregar a média das avaliações
    fetch(`http://localhost:8081/avaliacoes/media/${livroId}`, {
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
      "Content-Type": "application/json"
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`Erro: ${response.status}`);
    }
    return response.json();
  })
  .then(data => {
    console.log(data);
  })
  .catch(error => {
    console.error("Erro ao carregar média:", error);
  });
  }, [livroId, token]);

  const fazerReserva = () => {
    fetch("http://localhost:8081/reservas/criar", {
      method: "POST",
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ livroId }),
    })
      .then((res) => {
        if (res.ok) {
          setReservaMensagem("Reserva realizada com sucesso!");
        } else {
          throw new Error("Erro ao fazer reserva.");
        }
      })
      .catch((err) => {
        console.error(err);
        setReservaMensagem("Erro ao fazer reserva.");
      });
  };

  const enviarAvaliacao = () => {
    fetch("http://localhost:8081/avaliacoes/criar", {
      method: "POST",
      headers: {
        ...(token && { Authorization: `Bearer ${token}` }),
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
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
        console.error(err);
        setAvaliacaoMensagem("Erro ao enviar avaliação.");
      });
  };

  if (erro)
    return <div className="alert alert-danger text-center mt-4">{erro}</div>;
  if (!livro) return <div className="text-center mt-4">Carregando livro...</div>;

  return (
    <div className="livro-detalhes">
      <h2 className="titulo">{livro.titulo}</h2>
      <p><strong>Autor:</strong> {livro.autor}</p>
      <p><strong>Editora:</strong> {livro.editora}</p>
      <p><strong>Ano de Publicação:</strong> {livro.ano_publicacao}</p>
      <p><strong>Curso:</strong> {livro.curso}</p>
      <p><strong>Gênero:</strong> {livro.genero}</p>
      <p><strong>Quantidade disponível:</strong> {livro.quantidade}</p>
      <p><strong>Nota média:</strong> {livro.notaMedia ?? "Sem avaliações"}</p>
      <p><strong>Descrição:</strong> {livro.descricao}</p>

      <button className="botao-reservar" onClick={fazerReserva}>
        Reservar livro
      </button>
      {reservaMensagem && (
        <div className="alert alert-info mt-2">{reservaMensagem}</div>
      )}

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
                onChange={(e) => setNota(parseInt(e.target.value))}
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
            {avaliacaoMensagem && (
              <div className="alert alert-info mt-2">{avaliacaoMensagem}</div>
            )}
          </div>
        )}

        <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
      </div>
    </div>
  );
};

export default Livro;
