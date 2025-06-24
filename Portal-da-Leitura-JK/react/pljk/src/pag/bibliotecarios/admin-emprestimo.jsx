import React, { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/adminEmprestimo.css";

function formatDate(dateString) {
  if (!dateString) return "Data não disponível";
  const [year, month, day] = dateString.split("-");
  return `${day}/${month}/${year}`;
}

function EmprestimoRow({ emp, registrarDevolucao, renovarEmprestimo }) {
  const [diasSelecionados, setDiasSelecionados] = useState(7);

  return (
    <tr
      className={
        emp.dataDevolucao
          ? "linha-finalizado"
          : emp.vencido
          ? "linha-vencida"
          : "linha-ativa"
      }
    >
      <td>{emp.matricula || "N/A"}</td>
      <td>{emp.titulo || "N/A"}</td>
      <td>{formatDate(emp.dataEmprestimo)}</td>
      <td>{formatDate(emp.dataVencimento)}</td>
      <td>
        {emp.dataDevolucao
          ? formatDate(emp.dataDevolucao)
          : "Ainda não devolvido"}
      </td>
      <td>{emp.diasAtraso || 0}</td>
      <td>{emp.vencido ? "Sim" : "Não"}</td>
      <td>{emp.renovacoes}</td>
      <td>
        {emp.dataDevolucao ? (
          <span className="status-finalizado">Finalizado</span>
        ) : (
          <span className="status-ativo">Ativo</span>
        )}
      </td>

      <td>
        {!emp.dataDevolucao && (
          <>
            <button
              className="admin-btn admin-btn-devolver"
              onClick={() => registrarDevolucao(emp.emprestimoId)}
            >
              Devolução
            </button>

            <select
              className="admin-select-dias"
              value={diasSelecionados}
              onChange={(e) => setDiasSelecionados(Number(e.target.value))}
            >
              {Array.from({ length: 14 }, (_, i) => i + 7).map((dia) => (
                <option key={dia} value={dia}>
                  {dia} dias
                </option>
              ))}
            </select>

            <button
              className="admin-btn admin-btn-renovar"
              onClick={() =>
                renovarEmprestimo(emp.emprestimoId, diasSelecionados)
              }
            >
              Renovar
            </button>
          </>
        )}
      </td>
    </tr>
  );
}

export default function EmprestimosAdmin() {
  const { auth } = useAuth();
  const [emprestimos, setEmprestimos] = useState([]);
  const [error, setError] = useState(null);

  const carregarEmprestimos = async () => {
    if (!auth || !auth.token) return;

    try {
      const response = await fetch(`http://localhost:8081/emprestimos`, {
        headers: {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
      });

      if (response.status === 204) {
      setEmprestimos([]); // sem conteúdo, lista vazia
      return;
      }

      if (!response.ok) throw new Error("Erro ao carregar os empréstimos");

      const data = await response.json();

      const emprestimosComStatus = await Promise.all(
        data.map(async (emp) => {
          const [vencido, diasAtraso] = await Promise.all([
            verificarVencido(emp.emprestimoId),
            pegarDiasAtraso(emp.emprestimoId),
          ]);
          return {
            ...emp,
            vencido,
            diasAtraso,
          };
        })
      );

      setEmprestimos(emprestimosComStatus);
    } catch (err) {
      setError(err.message || "Erro inesperado.");
    }
  };

  useEffect(() => {
    carregarEmprestimos();
  }, [auth]);

  const registrarDevolucao = async (emprestimoId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/devolver`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${auth.token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Erro ao registrar devolução");

      carregarEmprestimos();
    } catch (err) {
      setError(err.message);
    }
  };

  const renovarEmprestimo = async (emprestimoId, dias = 7) => {
    if (!auth || !auth.token) {
      console.log("Sem token de autenticação");
      return;
    }

    const url = `http://localhost:8081/emprestimos/${emprestimoId}/renovar-admin`;
    const body = JSON.stringify({ dias });

    try {
      const response = await fetch(url, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        body,
      });

      if (!response.ok) throw new Error("Erro ao renovar empréstimo");

      carregarEmprestimos();
    } catch (err) {
      setError(err.message);
    }
  };

  const verificarVencido = async (emprestimoId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/vencido`,
        {
          headers: {
            Authorization: `Bearer ${auth.token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Erro ao verificar vencimento");

      const isVencido = await response.json();
      return isVencido;
    } catch {
      return false;
    }
  };

  const pegarDiasAtraso = async (emprestimoId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/atraso`,
        {
          headers: {
            Authorization: `Bearer ${auth.token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Erro ao pegar dias de atraso");

      const dias = await response.json();
      return dias;
    } catch {
      return 0;
    }
  };

  return (
    <div className="admin-emprestimos-container">
      <h1 className="admin-emprestimos-title">Empréstimos Registrados</h1>

      {error && <p className="admin-error-text">{error}</p>}

      <table className="admin-emprestimos-table">
        <thead>
          <tr>
            <th>Aluno</th>
            <th>Livro</th>
            <th>Data de Empréstimo</th>
            <th>Data Prevista de Devolução</th>
            <th>Data de Devolução</th>
            <th>Dias de Atraso</th>
            <th>Vencido</th>
            <th>Renovações</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {emprestimos.length === 0 ? (
            <tr>
              <td colSpan="10">Nenhum empréstimo encontrado.</td>
            </tr>
          ) : (
            emprestimos.map((emp) => (
              <EmprestimoRow
                key={emp.emprestimoId}
                emp={emp}
                registrarDevolucao={registrarDevolucao}
                renovarEmprestimo={renovarEmprestimo}
              />
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
