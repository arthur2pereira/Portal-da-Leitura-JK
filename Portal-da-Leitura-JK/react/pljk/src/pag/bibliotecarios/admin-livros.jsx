  import { useEffect, useState } from "react";
  import styles from "../../assets/css/adminLivros.module.css";
  import { useAuth } from "../../authContext.jsx";

  function GerenciarLivros() {
    const [livros, setLivros] = useState([]);
    const [filtro, setFiltro] = useState("");
    const [paginaAtual, setPaginaAtual] = useState(0);
    const [totalPaginas, setTotalPaginas] = useState(1);
    const { auth } = useAuth();

    const [formLivro, setFormLivro] = useState({
      livroId: null,
      titulo: "",
      autor: "",
      genero: "",
      curso: "",
      editora: "",
      anoPublicacao: "",
      descricao: "",
      quantidade: ""
    });

    const [modoEdicao, setModoEdicao] = useState(false);
    const [mensagem, setMensagem] = useState(null);

    useEffect(() => {
      buscarLivros(0);
    }, []);

    const buscarLivros = async (pagina = 0) => {
      try {
        const resposta = await fetch(`http://localhost:8081/livros/listar?pagina=${pagina}&tamanho=10`, {
          method: "GET",
          headers: {
            ...(auth?.token && { Authorization: `Bearer ${auth.token}` }),
            "Content-Type": "application/json"
          }
        });

        if (!resposta.ok) throw new Error(`Erro HTTP: ${resposta.status}`);

        const dados = await resposta.json();
        setLivros(dados.livros || []);
        setPaginaAtual(dados.paginaAtual);
        setTotalPaginas(dados.totalPaginas);
      } catch (err) {
        console.error("Erro ao buscar livros:", err);
        setMensagem({ tipo: "erro", texto: "Erro ao carregar livros." });
      }
    };

    const filtrarLivros = () => {
      return livros.filter((livro) =>
        livro.titulo.toLowerCase().includes(filtro.toLowerCase())
      );
    };  

    const handleChange = (e) => {
      setFormLivro({ ...formLivro, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
      e.preventDefault();

      if (formLivro.titulo.trim() === "" ||
        formLivro.autor.trim() === "" ||
        formLivro.genero.trim() === "" ||
        formLivro.editora.trim() === "" ||
        formLivro.descricao.trim() === "") {
        return setMensagem({ tipo: "erro", texto: "Todos os campos obrigatórios devem ser preenchidos." });
      }  

      const quantidadeInt = parseInt(formLivro.quantidade);
      const anoPublicacaoInt = parseInt(formLivro.anoPublicacao);
    
      if (isNaN(quantidadeInt) || quantidadeInt < 0) {
        return setMensagem({ tipo: "erro", texto: "Quantidade inválida." });
      }
    
      if (isNaN(anoPublicacaoInt)) {
        return setMensagem({ tipo: "erro", texto: "Ano de publicação inválido." });
      }

      const livroCorrigido = {
        ...formLivro,
        quantidade: quantidadeInt,
        anoPublicacao: anoPublicacaoInt
      };  

      console.log("Enviando livro:", livroCorrigido);

      try {
        const config = {
          method: modoEdicao ? "PUT" : "POST",
          headers: {
            "Content-Type": "application/json",
            ...(auth?.token && { Authorization: `Bearer ${auth.token}` })
          },
          body: JSON.stringify(livroCorrigido)
        };
        const url = modoEdicao
          ? `http://localhost:8081/bibliotecarios/livros/${formLivro.livroId}` // Alterado para o endpoint correto
          : "http://localhost:8081/bibliotecarios/livros";

        const resp = await fetch(url, config);

        if (!resp.ok) throw new Error("Erro ao salvar livro");

        setMensagem({ tipo: "sucesso", texto: modoEdicao ? "Livro atualizado!" : "Livro cadastrado!" });
        buscarLivros();
        setFormLivro({
          livroId: null,
          titulo: "",
          autor: "",
          genero: "",
          curso: "",
          editora: "",
          anoPublicacao: "",
          descricao: "",
          quantidade: ""
        });
        setModoEdicao(false);
      } catch (err) {
        console.error(err);
        setMensagem({ tipo: "erro", texto: "Erro ao salvar o livro." });
      }

      setTimeout(() => setMensagem(null), 20000);
    };

    const editarLivro = (livro) => {
      setFormLivro(livro);
      setModoEdicao(true);
      setMensagem(null);
    };

    const excluirLivro = async (id) => {
      if (window.confirm("Você tem certeza que deseja excluir este livro?")) {
        try {
          const resp = await fetch(`http://localhost:8081/bibliotecarios/livros/${id}`, {
            method: "DELETE",
            headers: {
              ...(auth?.token && { Authorization: `Bearer ${auth.token}` })
            }
          });
          if (!resp.ok) throw new Error("Erro ao excluir livro");
          setMensagem({ tipo: "sucesso", texto: "Livro excluído com sucesso!" });
          buscarLivros();
        } catch (err) {
          console.error(err);
          setMensagem({ tipo: "erro", texto: "Erro ao excluir o livro." });
        }
        setTimeout(() => setMensagem(null), 3000);
      }
    };

    return (
      <div className={styles.conteudoAdmin}>
        <section className={styles.secao}>
          <h2>📚 Lista de Livros</h2>
          <input
            type="text"
            placeholder="Buscar por título"
            value={filtro}
            onChange={(e) => {
              setFiltro(e.target.value);
              setPagina(1);
            }}
          />

          {mensagem && (
            <div
              className={
                mensagem.tipo === "erro"
                  ? styles.mensagemErro
                  : styles.mensagemSucesso
              }
            >
              {mensagem.texto}
            </div>
          )}

          <ul className={styles.listaLivros}>
            {filtrarLivros().map((livro) => (
              <li key={livro.livroId}>
                <div className={styles.infoLivro}>
                  <span>
                    #{livro.livroId} - {livro.titulo} (Qtd: {livro.quantidade})
                  </span>
                  <div className={styles.botoesLivro}>
                    <button onClick={() => editarLivro(livro)} className={styles.editarBtn}>
                      Editar
                    </button>
                    <button onClick={() => excluirLivro(livro.livroId)} className={styles.excluirBtn}>
                      Excluir
                    </button>
                  </div>
                </div>
              </li>
            ))}
          </ul>

          <div className={styles.botaoPaginacaoContainer}>
            {Array.from({ length: totalPaginas }, (_, i) => (
              <button
                key={i}
                disabled={paginaAtual === i}
                onClick={() => buscarLivros(i)}
                className={`${styles.botaoPagina} ${paginaAtual === i ? styles.ativo : ''}`}
              >
                {i + 1}
              </button>
            ))}
          </div>
        </section>

        <section className={styles.secao}>
          <h2>{modoEdicao ? "✏️ Editar Livro" : "➕ Cadastrar Livro"}</h2>
          <form onSubmit={handleSubmit} className={styles.formularioLivro}>
            <input name="titulo" type="text" placeholder="Título" value={formLivro.titulo} onChange={handleChange} required />
            <input name="autor" type="text" placeholder="Autor" value={formLivro.autor} onChange={handleChange} required />
            <input name="genero" type="text" placeholder="Gênero" value={formLivro.genero} onChange={handleChange} required />
            <input name="curso" type="text" placeholder="Curso" value={formLivro.curso} onChange={handleChange} />
            <input name="editora" type="text" placeholder="Editora" value={formLivro.editora} onChange={handleChange} required />
            <input name="anoPublicacao" type="text" placeholder="Ano de Publicação" value={formLivro.anoPublicacao} onChange={handleChange} required />
            <textarea name="descricao" placeholder="Descrição" value={formLivro.descricao} onChange={handleChange} required />
            <input name="quantidade" type="number" placeholder="Quantidade" value={formLivro.quantidade} onChange={handleChange} min="0" required />
            <button type="submit">{modoEdicao ? "Salvar Alterações" : "Cadastrar Livro"}</button>
          </form>
        </section>
      </div>
    );
  }

  export default GerenciarLivros;
