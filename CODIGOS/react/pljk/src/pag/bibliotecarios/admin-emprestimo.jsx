import React, { useEffect, useState } from "react";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/adminEmprestimo.css";
import Swal from "sweetalert2";

function formatarData(dateString) {
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
          ? "emprestimo-linha-finalizado"
          : emp.vencido
          ? "emprestimo-linha-vencida"
          : "emprestimo-linha-ativa"
      }
    >
      <td>{emp.matricula || "N/A"}</td>
      <td>{emp.nome || "N/A"}</td>
      <td>{emp.titulo || "N/A"}</td>
      <td>{formatarData(emp.dataEmprestimo)}</td>
      <td>{formatarData(emp.dataVencimento)}</td>
      <td>
        {emp.dataDevolucao
          ? formatarData(emp.dataDevolucao)
          : "Ainda não devolvido"}
      </td>
      <td>{emp.diasAtraso || 0}</td>
      <td>{emp.vencido ? "Sim" : "Não"}</td>
      <td>{emp.renovacoes}</td>
      <td>
        {emp.dataDevolucao ? (
          <span className="emprestimo-status-finalizado">Finalizado</span>
        ) : (
          <span className="emprestimo-status-ativo">Ativo</span>
        )}
      </td>
      <td>
        {!emp.dataDevolucao && (
          <div className="emprestimo-acoes">
            <button
              className="emprestimo-btn emprestimo-btn-devolver"
              onClick={() => registrarDevolucao(emp.emprestimoId)}
            >
              Devolver
            </button>

            <select
              className="emprestimo-select-dias"
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
              className="emprestimo-btn emprestimo-btn-renovar"
              onClick={() => renovarEmprestimo(emp.emprestimoId, diasSelecionados)}
            >
              Renovar
            </button>
          </div>
        )}
      </td>
    </tr>
  );
}

export default function EmprestimosAdmin() {
  const { auth } = useAuth();
  const [emprestimos, setEmprestimos] = useState([]);
  const [error, setError] = useState(null);
  const [busca, setBusca] = useState("");

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
        setEmprestimos([]);
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

      emprestimosComStatus.sort((a, b) => {
        if (!a.dataDevolucao && b.dataDevolucao) return -1;
        if (a.dataDevolucao && !b.dataDevolucao) return 1;
        return new Date(b.dataEmprestimo) - new Date(a.dataEmprestimo);
      });

      setEmprestimos(emprestimosComStatus);
    } catch (err) {
      setError(err.message || "Erro inesperado.");
    }
  };

  useEffect(() => {
    carregarEmprestimos();
  }, [auth]);

  const registrarDevolucao = async (emprestimoId) => {
    const confirmacao = await Swal.fire({
      title: "Registrar devolução?",
      text: "Deseja marcar este empréstimo como devolvido?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#0a7",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, devolver",
      cancelButtonText: "Cancelar",
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/devolver`,
        {
          method: "PUT",
          headers: { Authorization: `Bearer ${auth.token}` },
        }
      );

      if (!response.ok) throw new Error("Erro ao registrar devolução");

      Swal.fire("Sucesso!", "Empréstimo devolvido com sucesso.", "success");
      carregarEmprestimos();
    } catch (err) {
      console.error(err);
      Swal.fire("Erro", err.message || "Erro ao registrar devolução.", "error");
    }
  };

  const renovarEmprestimo = async (emprestimoId, dias = 7) => {
    const confirmacao = await Swal.fire({
      title: "Renovar empréstimo?",
      text: `Deseja renovar este empréstimo por mais ${dias} dias?`,
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#2980b9",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, renovar",
      cancelButtonText: "Cancelar",
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/renovar-admin`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${auth.token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ dias }),
        }
      );

      if (!res.ok) {
        const msg = await res.text();
        Swal.fire("Erro", msg || "Erro ao renovar empréstimo.", "error");
        return;
      }

      Swal.fire("Sucesso", `Empréstimo renovado por ${dias} dias!`, "success");
      carregarEmprestimos();
    } catch (err) {
      console.error(err);
      Swal.fire("Erro", "Erro ao renovar empréstimo.", "error");
    }
  };

  const verificarVencido = async (emprestimoId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/vencido`,
        { headers: { Authorization: `Bearer ${auth.token}` } }
      );
      if (!response.ok) throw new Error("Erro ao verificar vencimento");
      return await response.json();
    } catch {
      return false;
    }
  };

  const pegarDiasAtraso = async (emprestimoId) => {
    try {
      const response = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/atraso`,
        { headers: { Authorization: `Bearer ${auth.token}` } }
      );
      if (!response.ok) throw new Error("Erro ao pegar dias de atraso");
      return await response.json();
    } catch {
      return 0;
    }
  };

  const emprestimosFiltrados = emprestimos.filter(
    (emp) =>
      emp.nome?.toLowerCase().includes(busca.toLowerCase()) ||
      emp.titulo?.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <div className="emprestimo-admin-container">
      <h2>Gerenciamento de Empréstimos</h2>

      <input
        type="text"
        className="emprestimo-busca-input"
        placeholder="Buscar por aluno ou livro..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
      />

      {error && <div className="emprestimo-feedback erro">{error}</div>}

      <div className="emprestimos-table-wrapper">
        <table className="emprestimos-table">
          <thead>
            <tr>
              <th>Matrícula</th>
              <th>Aluno</th>
              <th>Livro</th>
              <th>Data de Empréstimo</th>
              <th>Data de Vencimento</th>
              <th>Data de Devolução</th>
              <th>Dias de Atraso</th>
              <th>Vencido</th>
              <th>Renovações</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {emprestimosFiltrados.length === 0 ? (
              <tr>
                <td colSpan="11" className="text-center">
                  Nenhum empréstimo encontrado.
                </td>
              </tr>
            ) : (
              emprestimosFiltrados.map((emp) => (
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
    </div>
  );
}
