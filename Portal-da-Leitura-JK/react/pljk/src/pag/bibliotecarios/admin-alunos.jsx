import { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../assets/css/alunoAdmin.css";

export default function AdminAlunos() {
  const [alunos, setAlunos] = useState([]);
  const [erro, setErro] = useState("");
  const [mensagemSucesso, setMensagemSucesso] = useState("");
  const [busca, setBusca] = useState("");
  const [alunoParaEditar, setAlunoParaEditar] = useState(null);
  const { auth } = useAuth();

  useEffect(() => {
    if (auth && auth.token) {
      fetchAlunos();
    }
  }, [auth]);

  const fetchAlunos = () => {
    fetch("http://localhost:8081/bibliotecarios/listaDeAlunos", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Erro ao buscar alunos");
        }
        return response.json();
      })
      .then((data) => setAlunos(data))
      .catch(() => setErro("Erro ao buscar alunos."));
  };

  const deletarAluno = (matricula) => {
    if (!window.confirm("Tem certeza que deseja deletar este aluno?")) return;

    fetch(`http://localhost:8081/bibliotecarios/alunos/${matricula}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`,
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Erro ao deletar aluno");
        setAlunos((prev) => prev.filter((a) => a.matricula !== matricula));
      })
      .catch(() => alert("Erro ao deletar aluno."));
  };

  const atualizarAluno = (e) => {
    e.preventDefault();

    fetch("http://localhost:8081/bibliotecarios/atualizar", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`,
      },
      body: JSON.stringify(alunoParaEditar),
    })
      .then((response) => {
        if (!response.ok) throw new Error("Erro ao atualizar aluno");
        return response.json();
      })
      .then((data) => {
        setAlunos((prev) =>
          prev.map((aluno) => (aluno.matricula === data.matricula ? data : aluno))
        );
        setAlunoParaEditar(null);
        setMensagemSucesso("Aluno atualizado com sucesso!");
        setTimeout(() => setMensagemSucesso(""), 4000);
      })
      .catch(() => alert("Erro ao atualizar aluno."));
  };

  const alunosFiltrados = alunos.filter(
    (a) =>
      a.nome.toLowerCase().includes(busca.toLowerCase()) ||
      a.matricula.includes(busca)
  );

  return (
    <div className="admin-alunos-wrapper container py-5">
      <h2 className="text-center text-success mb-4 fw-bold">Gerenciar Alunos</h2>

      <input
        type="text"
        placeholder="Buscar por nome ou matrícula..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        className="form-control form-control-lg mx-auto mb-4 busca-input"
        aria-label="Buscar alunos por nome ou matrícula"
      />

      {erro && <div className="alert alert-danger text-center">{erro}</div>}

      {mensagemSucesso && (
        <div className="alert alert-success text-center mx-auto w-50">
          {mensagemSucesso}
        </div>
      )}

      {alunosFiltrados.length === 0 ? (
        <p className="text-muted text-center">Nenhum aluno encontrado.</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-bordered table-hover align-middle shadow-sm">
            <thead className="table-success">
              <tr>
                <th>Nome</th>
                <th>Matrícula</th>
                <th>Email</th>
                <th className="text-center">Ações</th>
              </tr>
            </thead>
            <tbody>
              {alunosFiltrados.map((aluno) => (
                <tr key={aluno.matricula}>
                  <td>{aluno.nome}</td>
                  <td>{aluno.matricula}</td>
                  <td>{aluno.email}</td>
                  <td className="text-center">
                    <button
                      className="btn btn-sm btn-danger"
                      onClick={() => deletarAluno(aluno.matricula)}
                    >
                      Deletar
                    </button>
                    <button
                      className="btn btn-sm btn-warning ms-2"
                      onClick={() => setAlunoParaEditar(aluno)}
                    >
                      Alterar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal */}
      {alunoParaEditar && (
        <div
          className="modal show d-block"
          tabIndex={-1}
          role="dialog"
          aria-modal="true"
          aria-labelledby="modalTitle"
          onClick={(e) => e.target === e.currentTarget && setAlunoParaEditar(null)}
        >
          <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content shadow">
              <div className="modal-header bg-success text-white">
                <h5 className="modal-title" id="modalTitle">
                  Alterar Dados do Aluno
                </h5>
                <button
                  type="button"
                  className="btn-close btn-close-white"
                  aria-label="Fechar"
                  onClick={() => setAlunoParaEditar(null)}
                ></button>
              </div>
              <form onSubmit={atualizarAluno}>
                <div className="modal-body">
                  <div className="mb-3">
                    <label htmlFor="nome" className="form-label fw-semibold">
                      Nome
                    </label>
                    <input
                      id="nome"
                      type="text"
                      className="form-control"
                      value={alunoParaEditar.nome}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({ ...prev, nome: e.target.value }))
                      }
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="email" className="form-label fw-semibold">
                      Email
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="form-control"
                      value={alunoParaEditar.email}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({ ...prev, email: e.target.value }))
                      }
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="matricula" className="form-label fw-semibold">
                      Matrícula
                    </label>
                    <input
                      id="matricula"
                      type="text"
                      className="form-control"
                      value={alunoParaEditar.matricula}
                      readOnly
                    />
                  </div>
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setAlunoParaEditar(null)}
                  >
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-success">
                    Atualizar
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
