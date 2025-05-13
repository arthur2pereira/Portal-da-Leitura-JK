import { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/alunoAdmin.css";

export default function AdminAlunos() {
  const [alunos, setAlunos] = useState([]);
  const [erro, setErro] = useState("");
  const [busca, setBusca] = useState("");
  const [alunoParaEditar, setAlunoParaEditar] = useState(null);  // Novo estado
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
      .catch((err) => {
        console.error(err);
        setErro("Erro ao buscar alunos.");
      });
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
        if (!response.ok) {
          throw new Error("Erro ao deletar aluno");
        }
        setAlunos((prev) => prev.filter((a) => a.matricula !== matricula));
      })
      .catch((err) => {
        console.error(err);
        alert("Erro ao deletar aluno.");
      });
  };

  const atualizarAluno = (e) => {
    e.preventDefault();
    const updatedAluno = alunoParaEditar;

    fetch("http://localhost:8081/bibliotecarios/atualizar", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${auth.token}`,
      },
      body: JSON.stringify(updatedAluno),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Erro ao atualizar aluno");
        }
        return response.json();
      })
      .then((data) => {
        setAlunos((prev) =>
          prev.map((aluno) =>
            aluno.matricula === data.matricula ? data : aluno
          )
        );
        setAlunoParaEditar(null);
      })
      .catch((err) => {
        console.error(err);
        alert("Erro ao atualizar aluno.");
      });
  };

  const alunosFiltrados = alunos.filter(
    (a) =>
      a.nome.toLowerCase().includes(busca.toLowerCase()) ||
      a.matricula.includes(busca)
  );

  return (
    <div className="admin-alunos-container">
      <h2>Gerenciar Alunos</h2>

      <input
        type="text"
        placeholder="Buscar por nome ou matrícula..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        className="form-control my-3"
      />

      {erro && <div className="alert alert-danger">{erro}</div>}

      {alunosFiltrados.length === 0 ? (
        <p className="text-muted">Nenhum aluno encontrado.</p>
      ) : (
        <table className="table table-bordered">
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
      )}

      {/* Modal de edição */}
      {alunoParaEditar && (
        <div className="modal" style={{ display: "block" }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Alterar Dados do Aluno</h5>
                <button
                  type="button"
                  className="close"
                  onClick={() => setAlunoParaEditar(null)}
                >
                  ×
                </button>
              </div>
              <form onSubmit={atualizarAluno}>
                <div className="modal-body">
                  <div className="form-group">
                    <label>Nome</label>
                    <input
                      type="text"
                      className="form-control"
                      value={alunoParaEditar.nome}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({
                          ...prev,
                          nome: e.target.value,
                        }))
                      }
                    />
                  </div>
                  <div className="form-group">
                    <label>Email</label>
                    <input
                      type="email"
                      className="form-control"
                      value={alunoParaEditar.email}
                      onChange={(e) =>
                        setAlunoParaEditar((prev) => ({
                          ...prev,
                          email: e.target.value,
                        }))
                      }
                    />
                  </div>
                  <div className="form-group">
                    <label>Matricula</label>
                    <input
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
                  <button type="submit" className="btn btn-primary">
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
