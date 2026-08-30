import { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../assets/css/adminAluno.css";
import Swal from "sweetalert2";

export default function AdminAlunos() {
  const [alunos, setAlunos] = useState([]);
  const [erro, setErro] = useState("");
  const [mensagemSucesso, setMensagemSucesso] = useState("");
  const [busca, setBusca] = useState("");
  const [alunoParaEditar, setAlunoParaEditar] = useState(null);
  const { auth } = useAuth();

  useEffect(() => {
    if (auth && auth.token) fetchAlunos();
  }, [auth]);

  const fetchAlunos = () => {
    fetch("http://localhost:8081/bibliotecarios/listaDeAlunos", {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then(setAlunos)
      .catch(() => setErro("Erro ao buscar alunos."));
  };

  const deletarAluno = async (matricula) => {
    const confirmacao = await Swal.fire({
      title: "Deletar aluno?",
      text: "Tem certeza que deseja deletar este aluno?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, deletar",
      cancelButtonText: "Cancelar",
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(`http://localhost:8081/bibliotecarios/alunos/${matricula}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`,
        },
      });

      if (!res.ok) throw new Error();

      setAlunos((prev) => prev.filter((a) => a.matricula !== matricula));
      Swal.fire("Deletado!", "O aluno foi deletado com sucesso.", "success");
    } catch {
      Swal.fire("Erro", "Erro ao deletar aluno.", "error");
    }
  };

  const atualizarAluno = async (e) => {
    e.preventDefault();

    const confirmacao = await Swal.fire({
      title: "Atualizar aluno?",
      text: "Deseja realmente atualizar os dados deste aluno?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#28a745",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, atualizar",
      cancelButtonText: "Cancelar",
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch("http://localhost:8081/bibliotecarios/atualizar", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`,
        },
        body: JSON.stringify(alunoParaEditar),
      });

      if (!res.ok) throw new Error();
      const data = await res.json();

      setAlunos((prev) =>
        prev.map((a) => (a.matricula === data.matricula ? data : a))
      );
      setAlunoParaEditar(null);
      Swal.fire("Sucesso!", "Aluno atualizado com sucesso!", "success");
    } catch {
      Swal.fire("Erro", "Erro ao atualizar aluno.", "error");
    }
  };

  const alunosFiltrados = alunos.filter(
    (a) =>
      a.nome.toLowerCase().includes(busca.toLowerCase()) ||
      a.matricula.includes(busca)
  );

  return (
    <div className="admin-alunos-wrapper">
      <h2>Gerenciar Alunos</h2>

      <input
        type="text"
        className="form-control busca-input"
        placeholder="Buscar por nome ou matrícula..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        aria-label="Buscar alunos"
      />

      {erro && <div className="alert alert-danger admin-alunos-alert">{erro}</div>}

      {mensagemSucesso && (
        <div className="alert alert-success admin-alunos-alert">{mensagemSucesso}</div>
      )}

      {alunosFiltrados.length === 0 ? (
        <p className="text-center text-muted mt-4">Nenhum aluno encontrado.</p>
      ) : (
        <div className="alunos-table-wrapper">
          <table className="alunos-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Matrícula</th>
                <th>Email</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {alunosFiltrados.map((aluno) => (
                <tr key={aluno.matricula}>
                  <td>{aluno.nome}</td>
                  <td>{aluno.matricula}</td>
                  <td>{aluno.email}</td>
                  <td>
                    <button
                      className="btn btn-sm btn-danger me-2"
                      onClick={() => deletarAluno(aluno.matricula)}
                    >
                      Deletar
                    </button>
                    <button
                      className="btn btn-sm btn-warning"
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

      {alunoParaEditar && (
        <div
          className="modal show d-block"
          tabIndex={-1}
          role="dialog"
          onClick={(e) => e.target === e.currentTarget && setAlunoParaEditar(null)}
        >
          <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Alterar Dados do Aluno</h5>
                <button
                  type="button"
                  className="btn-close btn-close-white"
                  onClick={() => setAlunoParaEditar(null)}
                  aria-label="Fechar"
                ></button>
              </div>
              <form onSubmit={atualizarAluno}>
                <div className="modal-body">
                  <div className="mb-3">
                    <label htmlFor="nome" className="form-label">
                      Nome
                    </label>
                    <input
                      id="nome"
                      type="text"
                      className="form-control"
                      value={alunoParaEditar.nome}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({
                          ...prev,
                          nome: e.target.value,
                        }))
                      }
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="email" className="form-label">
                      Email
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="form-control"
                      value={alunoParaEditar.email}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({
                          ...prev,
                          email: e.target.value,
                        }))
                      }
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="matricula" className="form-label">
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
