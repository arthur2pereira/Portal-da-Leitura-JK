import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../authContext";
import "../assets/css/livro.css";

const Livro = () => {
  const { livroId } = useParams();
  const { auth } = useAuth();
  const token = auth?.token;
  const matricula = auth?.matricula;

  const [livro, setLivro] = useState(null);
  const [erro, setErro] = useState("");
  const [mensagemReserva, setMensagemReserva] = useState("");
  const [mensagemAvaliacao, setMensagemAvaliacao] = useState("");

  const [avaliacoes, setAvaliacoes] = useState([]);
  const [mediaNota, setMediaNota] = useState(null);
  const [quantidadeDisponivel, setQuantidadeDisponivel] = useState(null);

  const [temReservaAtiva, setTemReservaAtiva] = useState(false);
  const [temEmprestimoAtivo, setTemEmprestimoAtivo] = useState(false);
  const [jaComentou, setJaComentou] = useState(false);
  const [podeAvaliar, setPodeAvaliar] = useState(false);

  const [mostrarFormAvaliacao, setMostrarFormAvaliacao] = useState(false);
  const [notaAvaliacao, setNotaAvaliacao] = useState(5);
  const [comentarioAvaliacao, setComentarioAvaliacao] = useState("");

  // Buscar dados do livro
  useEffect(() => {
    setErro(""); 
    setLivro(null);
    fetch(`http://localhost:8081/livros/${livroId}`, {
      headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : Promise.reject()))
      .then(setLivro)
      .catch(() => setErro("Não foi possível carregar o livro."));
  }, [livroId, token]);

  // Média de avaliações
  useEffect(() => {
    fetch(`http://localhost:8081/livros/avaliacao/media/${livroId}`, {
      headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : null))
      .then(setMediaNota)
      .catch(() => setMediaNota(null));
  }, [livroId, token]);

  // Avaliações e verificar se usuário já comentou
  useEffect(() => {
    setAvaliacoes([]);
    setJaComentou(false);
    fetch(`http://localhost:8081/livros/avaliacoes/${livroId}`, {
      headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : []))
      .then((data) => {
        setAvaliacoes(data);
        if (matricula) {
          const comentarioDoUsuario = data.find(av => av.matricula === matricula);
          setJaComentou(!!comentarioDoUsuario);
        } else {
          setJaComentou(false);
        }
      })
      .catch(() => setAvaliacoes([]));
  }, [livroId, token, matricula]);

  // Quantidade disponível
  useEffect(() => {
    fetch(`http://localhost:8081/livros/quantidade-disponivel/${livroId}`, {
      headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : 0))
      .then(setQuantidadeDisponivel)
      .catch(() => setQuantidadeDisponivel(0));
  }, [livroId, token]);

  // Reserva e empréstimo ativos
  useEffect(() => {
    if (!token || !matricula) return;

    fetch(`http://localhost:8081/alunos/${matricula}/temReservaAtiva`, {
      headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : false))
      .then(setTemReservaAtiva)
      .catch(() => setTemReservaAtiva(false));

    fetch(`http://localhost:8081/alunos/${matricula}/temEmprestimoAtivo`, {
      headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : false))
      .then(setTemEmprestimoAtivo)
      .catch(() => setTemEmprestimoAtivo(false));
  }, [token, matricula]);

  // Verificar se usuário já pegou o livro e pode avaliar
  useEffect(() => {
    if (!token || !matricula) return;

    fetch(`http://localhost:8081/alunos/${matricula}/jaPegouLivro/${livroId}`, {
      headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
    })
      .then((res) => (res.ok ? res.json() : false))
      .then((temEmprestimoDoLivro) => setPodeAvaliar(temEmprestimoDoLivro && !jaComentou))
      .catch(() => setPodeAvaliar(false));
  }, [token, matricula, livroId, jaComentou]);

  const handleReserva = () => {
    if (!token || !matricula) {
      setMensagemReserva("Você precisa estar logado para reservar um livro.");
      return;
    }

    fetch("http://localhost:8081/reservas/criar", {
      method: "POST",
      headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
      body: JSON.stringify({ matricula, livroId }),
    })
      .then((res) => {
        if (res.ok) {
          setMensagemReserva("Reserva realizada com sucesso!");
          setQuantidadeDisponivel((qtd) => (qtd !== null ? qtd - 1 : qtd));
          setTemReservaAtiva(true);
        } else {
          return res.text().then((text) => Promise.reject(new Error(text || "Erro ao fazer reserva.")));
        }
      })
      .catch((err) => {
        setMensagemReserva(err.message.includes("já possui uma reserva ativa")
          ? "Você já possui uma reserva ativa."
          : "Erro ao fazer reserva.");
      });
  };

  const handleEnviarAvaliacao = () => {
    if (!token || !matricula) return;

    fetch("http://localhost:8081/avaliacoes/criar", {
      method: "POST",
      headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
      body: JSON.stringify({ matricula, livroId, nota: notaAvaliacao, comentario: comentarioAvaliacao }),
    })
      .then((res) => {
        if (res.ok) {
          setMensagemAvaliacao("Avaliação enviada com sucesso!");
          setComentarioAvaliacao("");
          setNotaAvaliacao(5);
          setMostrarFormAvaliacao(false);

          // Atualizar avaliações e média
          fetch(`http://localhost:8081/livros/avaliacoes/${livroId}`, {
            headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
          })
            .then((res) => (res.ok ? res.json() : []))
            .then(setAvaliacoes)
            .catch(() => setAvaliacoes([]));

          fetch(`http://localhost:8081/livros/avaliacao/media/${livroId}`, {
            headers: { ...(token && { Authorization: `Bearer ${token}` }), "Content-Type": "application/json" },
          })
            .then((res) => (res.ok ? res.json() : null))
            .then(setMediaNota)
            .catch(() => setMediaNota(null));
        } else throw new Error("Erro ao enviar avaliação.");
      })
      .catch(() => setMensagemAvaliacao("Erro ao enviar avaliação."));
  };

  const mostrarCampo = (campo, textoPadrao = "Não informado") =>
    campo === null || campo === undefined || campo === "" ? textoPadrao : campo;

  if (erro) return <div className="alert alerta-erro text-center mt-4">{erro}</div>;
  if (!livro) return <div className="texto-carregando text-center mt-4">Carregando livro...</div>;

  return (
    <section className="detalhes-livro">
      <h2 className="titulo-livro">{mostrarCampo(livro.titulo)}</h2>
      <p><strong>Autor:</strong> {mostrarCampo(livro.autor)}</p>
      <p><strong>Editora:</strong> {mostrarCampo(livro.editora)}</p>
      <p><strong>Ano de Publicação:</strong> {mostrarCampo(livro.anoPublicacao)}</p>
      <p><strong>Curso:</strong> {mostrarCampo(livro.curso)}</p>
      <p><strong>Gênero:</strong> {mostrarCampo(livro.genero)}</p>
      <p><strong>Quantidade total:</strong> {mostrarCampo(livro.quantidade)}</p>
      <p><strong>Disponível para reserva:</strong> {quantidadeDisponivel !== null ? quantidadeDisponivel : "Carregando..."}</p>
      <p><strong>Nota média:</strong> {mediaNota !== null ? mediaNota.toFixed(2) : "Sem avaliações"}</p>
      <p><strong>Descrição:</strong> {mostrarCampo(livro.descricao)}</p>

      {temReservaAtiva || temEmprestimoAtivo ? (
        <div className="alert alerta-aviso">Você já possui {temReservaAtiva ? "uma reserva" : "um empréstimo"} ativo. Não é possível reservar outro livro.</div>
      ) : quantidadeDisponivel === 0 ? (
        <div className="alert alerta-erro">Não há exemplares disponíveis para reserva no momento.</div>
      ) : (
        <button className="botao-reservar" onClick={handleReserva}>Reservar livro</button>
      )}
      {mensagemReserva && <div className="alert alerta-info mt-2">{mensagemReserva}</div>}

      <section className="avaliacoes-livro mt-4">
        <h4>Avaliações ({avaliacoes.length})</h4>

        {!matricula && avaliacoes.length > 0 && (
          <div className="alert alerta-aviso mt-2">
            Este livro tem {avaliacoes.length} {avaliacoes.length === 1 ? "avaliação" : "avaliações"}. Faça login para ver os comentários.
          </div>
        )}

        {!matricula && avaliacoes.length === 0 && (
          <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
        )}

        {token && podeAvaliar && (
          <button className="botao-toggle-avaliacao" onClick={() => setMostrarFormAvaliacao(prev => !prev)}>
            {mostrarFormAvaliacao ? "Cancelar avaliação" : "Fazer avaliação"}
          </button>
        )}

        {mostrarFormAvaliacao && token && podeAvaliar && (
          <form className="form-avaliacao" onSubmit={(e) => { e.preventDefault(); handleEnviarAvaliacao(); }}>
            {/* ...mantém igual... */}
          </form>
        )}

        {!podeAvaliar && jaComentou && (
          <div className="alert alerta-aviso mt-2">Você já comentou este livro e não pode enviar outro comentário.</div>
        )}

        {matricula && avaliacoes.length === 0 && (
          <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
        )}

        {matricula && avaliacoes.length > 0 && (
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
