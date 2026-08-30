import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Select from 'react-select';
import '../assets/css/catalogo.css';

function Catalogo() {
  const navigate = useNavigate();
  const location = useLocation();

  const [paginaAtual, setPaginaAtual] = useState(1);
  const [filtros, setFiltros] = useState({
    curso: '',
    genero: '',
    autor: location.state?.autor || '',
    editora: location.state?.editora || '',
    pesquisa: ''
  });
  const [livros, setLivros] = useState([]);
  const [totalPaginas, setTotalPaginas] = useState(1);

  const [cursos, setCursos] = useState([]);
  const [generos, setGeneros] = useState([]);
  const [autores, setAutores] = useState([]);
  const [editoras, setEditoras] = useState([]);

  const livrosPorPagina = 12;

  useEffect(() => {
    if(location.state) {
      setFiltros(prev => ({
        ...prev,
        autor: location.state.autor || '',
        editora: location.state.editora || '',
      }));
      setPaginaAtual(1);
    }
  }, [location.state]);
  
  const formatarOpcoes = (lista) => lista.map(item => ({ value: item, label: item }));

  const handleFiltroChange = (e) => {
    const { name, value } = e.target;
    setFiltros(prev => ({ ...prev, [name]: value }));
    setPaginaAtual(1);
  };

  const montarQueryString = () => {
    const params = new URLSearchParams();
    params.append("pagina", isNaN(paginaAtual) ? 0 : paginaAtual - 1);
    params.append("tamanho", livrosPorPagina);
    if (filtros.curso) params.append("curso", filtros.curso);
    if (filtros.genero) params.append("genero", filtros.genero);
    if (filtros.autor) params.append("autor", filtros.autor);
    if (filtros.editora) params.append("editora", filtros.editora);
    return params.toString();
  };

  useEffect(() => {
    const fetchLivros = async () => {
      try {
        const token = localStorage.getItem('token');
        let endpoint = '';
        if (filtros.pesquisa && filtros.pesquisa.trim() !== '') {
          endpoint = `http://localhost:8081/livros/buscar?titulo=${encodeURIComponent(filtros.pesquisa)}&pagina=${paginaAtual - 1}&tamanho=${livrosPorPagina}`;
        } else {
          const queryString = montarQueryString();
          endpoint = `http://localhost:8081/livros/filtrar?${queryString}`;
        }

        const response = await fetch(endpoint, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);
        const data = await response.json();

        setLivros(data.livros || []);
        setTotalPaginas(data.totalPaginas || 1);
        if (typeof data.paginaAtual === 'number') {
          setPaginaAtual(data.paginaAtual + 1);
        }
      } catch (error) {
        setLivros([]);
        setTotalPaginas(1);
        console.error("Erro ao buscar livros:", error);
      }
    };
    fetchLivros();
  }, [filtros, paginaAtual]);

  // Carrega filtros (cursos, autores, editoras, gêneros)
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
        const cursosJson = await resCursos.json();
        const generosJson = await resGeneros.json();
        const autoresJson = await resAutores.json();
        const editorasJson = await resEditoras.json();

        setCursos(cursosJson.filter(c => c !== null).sort((a, b) => a.localeCompare(b)));
        setGeneros(generosJson.filter(g => g !== null).sort((a, b) => a.localeCompare(b)));
        setAutores(autoresJson.filter(a => a !== null && a.toLowerCase() !== 'diversos').sort((a, b) => a.localeCompare(b)));
        setEditoras(editorasJson.filter(e => e !== null).sort((a, b) => a.localeCompare(b)));
      } catch (error) {
        console.error("Erro ao carregar filtros:", error);
      }
    };
    fetchFiltros();
  }, []);

  // Filtro case-insensitive e parcial
  const livrosFiltrados = livros.filter(livro =>
    (!filtros.autor || (livro.autor || '').toLowerCase().includes(filtros.autor.toLowerCase())) &&
    (!filtros.editora || (livro.editora || '').toLowerCase().includes(filtros.editora.toLowerCase())) &&
    (!filtros.curso || (livro.curso || '').toLowerCase().includes(filtros.curso.toLowerCase())) &&
    (!filtros.genero || (livro.genero || '').toLowerCase().includes(filtros.genero.toLowerCase())) &&
    (!filtros.pesquisa || (livro.titulo || '').toLowerCase().includes(filtros.pesquisa.toLowerCase()))
  );

  return (
    <div className="catalogo-area">
      <aside className="catalogo-filtros">
        <h3>Filtros</h3>
        <input
          type="text"
          placeholder="Pesquisar por título"
          name="pesquisa"
          value={filtros.pesquisa}
          onChange={handleFiltroChange}
        />
        <Select
          name="curso"
          placeholder="Nenhum curso selecionado"
          isClearable
          options={formatarOpcoes(cursos)}
          value={filtros.curso ? { value: filtros.curso, label: filtros.curso } : null}
          onChange={(opcao) => handleFiltroChange({ target: { name: 'curso', value: opcao?.value || '' } })}
          noOptionsMessage={() => 'Nenhum curso disponível'}
        />
        <Select
          name="autor"
          placeholder="Nenhum autor selecionado"
          isClearable
          options={formatarOpcoes(autores)}
          value={filtros.autor ? { value: filtros.autor, label: filtros.autor } : null}
          onChange={(opcao) => handleFiltroChange({ target: { name: 'autor', value: opcao?.value || '' } })}
          noOptionsMessage={() => 'Nenhum autor disponível'}
        />
        <Select
          name="editora"
          placeholder="Nenhuma editora selecionada"
          isClearable
          options={formatarOpcoes(editoras)}
          value={filtros.editora ? { value: filtros.editora, label: filtros.editora } : null}
          onChange={(opcao) => handleFiltroChange({ target: { name: 'editora', value: opcao?.value || '' } })}
          noOptionsMessage={() => 'Nenhuma editora disponível'}
        />
        <Select
          name="genero"
          placeholder="Nenhum gênero selecionado"
          isClearable
          options={formatarOpcoes(generos)}
          value={filtros.genero ? { value: filtros.genero, label: filtros.genero } : null}
          onChange={(opcao) => handleFiltroChange({ target: { name: 'genero', value: opcao?.value || '' } })}
          noOptionsMessage={() => 'Nenhum gênero disponível'}
        />
      </aside>
      <main className="catalogo-livros">
        <div className="catalogo-grade">
          {livrosFiltrados.length > 0 ? (
            livrosFiltrados.map((livro) => (
              <div
                key={livro.livroId}
                className="catalogo-card"
                onClick={() => navigate(`/livro/${livro.livroId}`)}
              >
                <h4>{livro.titulo}</h4>
                <p><strong>Autor:</strong> {livro.autor}</p>
                <p><strong>Editora:</strong> {livro.editora}</p>
                <p>{livro.descricao}</p>
              </div>
            ))
          ) : (
            <p>Nenhum livro encontrado.</p>
          )}
        </div>
        <div className="catalogo-paginacao">
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
