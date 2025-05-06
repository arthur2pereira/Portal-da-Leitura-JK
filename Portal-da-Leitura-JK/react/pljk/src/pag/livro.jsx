import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../authContext";
import "../assets/css/livro.css"; // Importa o CSS

const Livro = () => {
  const { livroId } = useParams();
  const [livro, setLivro] = useState(null);
  const [erro, setErro] = useState("");
  const { token } = useAuth();

  useEffect(() => {
    axios
      .get(`http://localhost:8081/livros/${livroId}`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
      .then((res) => setLivro(res.data))
      .catch((err) => {
        console.error(err);
        setErro("Não foi possível carregar o livro.");
      });
  }, [livroId, token]);

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
      <p><strong>Quantidade disponível:</strong> {livro.quantidade}</p>
      <p><strong>Descrição:</strong> {livro.descricao}</p>

      <div className="avaliacoes">
        <h4>Avaliações</h4>
        <p className="sem-avaliacoes">Ainda não há avaliações para este livro.</p>
      </div>
    </div>
  );
};

export default Livro;
