import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/css/catalogo.css';

function Catalogo() {
  const navigate = useNavigate();
  const [filtros, setFiltros] = useState({
    curso: '',
    genero: '',
    autor: '',
    editora: '',
    pesquisa: ''
  });

  const [paginaAtual, setPaginaAtual] = useState(1);
  const [livros, setLivros] = useState([]);
  const [totalPaginas, setTotalPaginas] = useState(1);

  const [cursos, setCursos] = useState([]);
  const [generos, setGeneros] = useState([]);
  const [autores, setAutores] = useState([]);
  const [editoras, setEditoras] = useState([]);

  const livrosPorPagina = 20;

  const handleFiltroChange = (e) => {
    const { name, value } = e.target;
    setFiltros(prev => ({ ...prev, [name]: value }));
    setPaginaAtual(1);
  };

  const montarQueryString = () => {
    const params = new URLSearchParams();
    params.append("pagina", paginaAtual);
    params.append("tamanho", livrosPorPagina);
    if (filtros.pesquisa) params.append("titulo", filtros.pesquisa);
    if (filtros.curso) params.append("curso", filtros.curso);
    if (filtros.genero) params.append("genero", filtros.genero);
    if (filtros.autor) params.append("autor", filtros.autor);
    if (filtros.editora) params.append("editora", filtros.editora);
    return params.toString();
  };

  useEffect(() => {
    const fetchLivros = async () => {
      try {
        const queryString = montarQueryString();
        const token = localStorage.getItem('token');  // Supondo que o token esteja armazenado no localStorage
        const response = await fetch(`http://localhost:8081/livros/buscar?${queryString}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,  // Passando o token no cabeçalho
          },
        });
        if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
        const data = await response.json();
        setLivros(data || []);
        setTotalPaginas(1);        
      } catch (error) {
        console.error("Erro ao buscar livros:", error);
      }
    };    
    fetchLivros();
  }, [filtros, paginaAtual]);

  useEffect(() => {
    const fetchFiltros = async () => {
      try {
        const responses = await Promise.all([
          fetch('http://localhost:8081/livros/cursos'),
          fetch('http://localhost:8081/livros/generos'),
          fetch('http://localhost:8081/livros/autores'),
          fetch('http://localhost:8081/livros/editoras')
        ]);
        const [resCursos, resGeneros, resAutores, resEditoras] = responses;
        if (!resCursos.ok || !resGeneros.ok || !resAutores.ok || !resEditoras.ok) {
          throw new Error("Falha ao carregar um ou mais filtros.");
        }
        setCursos(await resCursos.json());
        setGeneros(await resGeneros.json());
        setAutores(await resAutores.json());
        setEditoras(await resEditoras.json());
      } catch (error) {
        console.error("Erro ao carregar filtros:", error);
      }
    };
    fetchFiltros(); 
  }, []);

  return (
    <div className="catalogo-wrapper">
      <aside className="sidebar">
        <h3>Filtros</h3>

        <input
          type="text"
          placeholder="Pesquisar por título ou autor"
          name="pesquisa"
          value={filtros.pesquisa}
          onChange={handleFiltroChange}
        />

        <select name="curso" value={filtros.curso} onChange={handleFiltroChange}>
          <option value="">Todos os cursos</option>
          {cursos.map((curso, i) => (
            <option key={curso} value={curso}>{curso}</option>
          ))}
        </select>

        <select name="autor" value={filtros.autor} onChange={handleFiltroChange}>
          <option value="">Todos os autores</option>
          {autores.map((autor, i) => (
            <option key={autor} value={autor}>{autor}</option>
          ))}
        </select>

        <select name="editora" value={filtros.editora} onChange={handleFiltroChange}>
          <option value="">Todas as editoras</option>
          {editoras.map((editora, i) => (
            <option key={editora} value={editora}>{editora}</option>
          ))}
        </select>

        <select name="genero" value={filtros.genero} onChange={handleFiltroChange}>
          <option value="">Todos os gêneros</option>
          {generos.map((genero, i) => (
            <option key={genero} value={genero}>{genero}</option>
          ))}
        </select>
      </aside>

      <main className="livros-container">
        <div className="lista-livros">
          {livros.length > 0 ? (
            livros.map((livro) => (
              <div
                key={livro.livroId}
                className="card-livro"
                onClick={() => navigate(`/livro/${livro.livroId}`)}
                style={{ cursor: 'pointer' }} // Opcional: mostra que o card é clicável
              >
                <h4>{livro.titulo}</h4>
                <p><strong>Autor:</strong> {livro.autor}</p>
                <p>{livro.descricao}</p>
              </div>
            ))
          ) : (
            <p>Nenhum livro encontrado.</p>
          )}
        </div>

        <div className="paginacao">
          {Array.from({ length: totalPaginas }, (_, i) => (
            <button
              key={i + 1}
              className={paginaAtual === i + 1 ? 'ativo' : ''}
              onClick={() => setPaginaAtual(i + 1)}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </main>
    </div>
  );
}

export default Catalogo;
