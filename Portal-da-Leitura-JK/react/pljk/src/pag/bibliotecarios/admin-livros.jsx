import { useEffect, useState } from "react"
import { useAuth } from "../../authContext.jsx"
import { Book } from "lucide-react"
import "bootstrap/dist/css/bootstrap.min.css"
import "../../assets/css/adminLivros.css"

export default function AdminLivros() {
  const [livros, setLivros] = useState([])
  const [filtro, setFiltro] = useState("")
  const [paginaAtual, setPaginaAtual] = useState(0)
  const [totalPaginas, setTotalPaginas] = useState(1)
  const [modoEdicao, setModoEdicao] = useState(false)
  const [mensagem, setMensagem] = useState(null)
  const { auth } = useAuth()

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
  })

  useEffect(() => {
    if (auth?.token) buscarLivros()
  }, [auth])

  const buscarLivros = async (pagina = 0) => {
    try {
      const res = await fetch(`http://localhost:8081/livros/listar?pagina=${pagina}&tamanho=10`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`
        }
      })
      if (!res.ok) throw new Error()
      const dados = await res.json()
      setLivros(dados.livros || [])
      setPaginaAtual(dados.paginaAtual)
      setTotalPaginas(dados.totalPaginas)
    } catch {
      setMensagem({ tipo: "erro", texto: "Erro ao buscar livros" })
    }
  }

  const filtrarLivros = () =>
    livros.filter((livro) =>
      livro.titulo.toLowerCase().includes(filtro.toLowerCase())
    )

  const handleChange = (e) =>
    setFormLivro({ ...formLivro, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()

    const url = modoEdicao
      ? `http://localhost:8081/bibliotecarios/livros/${formLivro.livroId}`
      : "http://localhost:8081/bibliotecarios/livros"

    const config = {
      method: modoEdicao ? "PUT" : "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`
      },
      body: JSON.stringify({
        ...formLivro,
        quantidade: parseInt(formLivro.quantidade),
        anoPublicacao: parseInt(formLivro.anoPublicacao)
      })
    }

    try {
      const res = await fetch(url, config)
      if (!res.ok) throw new Error()
      setMensagem({
        tipo: "sucesso",
        texto: modoEdicao ? "Livro atualizado!" : "Livro cadastrado!"
      })
      buscarLivros()
      setModoEdicao(false)
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
      })
    } catch {
      setMensagem({ tipo: "erro", texto: "Erro ao salvar livro" })
    }

    setTimeout(() => setMensagem(null), 3000)
  }

  const editarLivro = (livro) => {
    setModoEdicao(true)
    setFormLivro(livro)
    setMensagem(null)
  }

  const cancelarEdicao = () => {
    setModoEdicao(false)
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
    })
    setMensagem(null)
  }

  const excluirLivro = async (id) => {
    if (!window.confirm("Tem certeza que deseja excluir este livro?")) return
    try {
      const res = await fetch(`http://localhost:8081/bibliotecarios/livros/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${auth.token}`
        }
      })
      if (!res.ok) throw new Error()
      setMensagem({ tipo: "sucesso", texto: "Livro excluído com sucesso!" })
      buscarLivros()
    } catch {
      setMensagem({ tipo: "erro", texto: "Erro ao excluir livro" })
    }

    setTimeout(() => setMensagem(null), 3000)
  }

  if (!auth || !auth.token) {
    return (
      <main className="py-5 text-center">
        <p className="text-danger fw-bold">Usuário não autenticado. Faça login.</p>
      </main>
    )
  }

  return (
    <main className="admin-livros-wrapper bg-white px-3 px-md-5">
      <header className="mb-5 text-center">
        <h2 className="fw-bold text-success d-flex align-items-center justify-content-center gap-2">
          <Book size={28} /> Gerenciar Livros
        </h2>

        {mensagem && (
          <div className={`alert mt-4 ${mensagem.tipo === "erro" ? "alert-danger" : "alert-success"}`}>
            {mensagem.texto}
          </div>
        )}

        <input
          type="text"
          placeholder="Buscar por título..."
          className="form-control mt-4 w-100 w-md-50 mx-auto"
          value={filtro}
          onChange={(e) => setFiltro(e.target.value)}
          style={{ height: "48px" }} // input maior
        />
      </header>

      <section className="row g-4 justify-content-center">
        {filtrarLivros().map((livro) => (
          <div className="col-sm-10 col-md-6 col-lg-4" key={livro.livroId}>
            <div className="border rounded-4 p-4 shadow-sm h-100 d-flex flex-column justify-content-between"
                 style={{ transition: "transform 0.3s ease", cursor: "pointer" }}
                 onMouseEnter={e => e.currentTarget.style.transform = "translateY(-6px)"}
                 onMouseLeave={e => e.currentTarget.style.transform = "translateY(0)"}
            >
              <div>
                <h5 className="fw-bold text-success mb-2">{livro.titulo}</h5>
                <ul className="list-unstyled text-muted small">
                  <li><strong>Autor:</strong> {livro.autor}</li>
                  <li><strong>Gênero:</strong> {livro.genero}</li>
                  <li><strong>Curso:</strong> {livro.curso || "-"}</li>
                  <li><strong>Editora:</strong> {livro.editora}</li>
                  <li><strong>Ano:</strong> {livro.anoPublicacao}</li>
                  <li><strong>Qtd:</strong> {livro.quantidade}</li>
                </ul>
                <p className="text-secondary mt-2" style={{ fontSize: "0.9rem" }}>{livro.descricao}</p>
              </div>
              <div className="d-flex justify-content-end gap-2 mt-3">
                <button className="btn btn-sm btn-outline-warning" onClick={() => editarLivro(livro)}>Editar</button>
                <button className="btn btn-sm btn-outline-danger" onClick={() => excluirLivro(livro.livroId)}>Excluir</button>
              </div>
            </div>
          </div>
        ))}
      </section>

      {totalPaginas > 1 && (
        <div className="d-flex justify-content-center mt-5 flex-wrap gap-2">
          {Array.from({ length: totalPaginas }, (_, i) => (
            <button
              key={i}
              className={`btn btn-sm ${i === paginaAtual ? "btn-success" : "btn-outline-success"}`}
              onClick={() => buscarLivros(i)}
            >
              {i + 1}
            </button>
          ))}
        </div>
      )}

      <section
        className="mt-5 p-4 border rounded-4 shadow-sm mx-auto"
        style={{ maxWidth: "900px" }}
      >
        <h4 className="text-center fw-bold mb-4">{modoEdicao ? "Editar Livro" : "Cadastrar Livro"}</h4>
        <form className="row g-3" onSubmit={handleSubmit}>
          {["titulo", "autor", "genero", "curso", "editora", "anoPublicacao", "quantidade"].map((field, i) => (
            <div className="col-md-6" key={i}>
              <input
                type={["anoPublicacao", "quantidade"].includes(field) ? "number" : "text"}
                className="form-control"
                name={field}
                placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                value={formLivro[field]}
                onChange={handleChange}
                required={field !== "curso"}
                style={{ height: "48px" }} // input maior
              />
            </div>
          ))}
          <div className="col-12">
            <textarea
              name="descricao"
              className="form-control"
              placeholder="Descrição"
              value={formLivro.descricao}
              onChange={handleChange}
              required
              style={{ minHeight: "100px", resize: "vertical" }}
            />
          </div>
          <div className="col-12 text-center d-flex justify-content-center gap-3">
            {modoEdicao && (
              <button
                type="button"
                className="btn btn-secondary"
                onClick={cancelarEdicao}
              >
                Cancelar edição
              </button>
            )}
            <button className="btn btn-success px-5" type="submit">
              {modoEdicao ? "Salvar Alterações" : "Cadastrar Livro"}
            </button>
          </div>
        </form>
      </section>
    </main>
  )
}
