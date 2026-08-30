import React, { useState, useEffect } from 'react';
import { useAuth } from '../../authContext.jsx';
import '../../assets/css/adminReserva.css';
import Swal from "sweetalert2";

function formatDate(dateString) {
  if (!dateString) return 'Data não disponível';
  const [year, month, day] = dateString.split('-');
  return new Date(year, month - 1, day).toLocaleDateString('pt-BR');
}

export default function ReservasAdmin() {
  const { auth } = useAuth();
  const [reservasList, setReservasList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);
  const [feedbackMsg, setFeedbackMsg] = useState(null);
  const [busca, setBusca] = useState('');

  const carregarReservas = async () => {
    if (!auth?.token) return;
    try {
      const response = await fetch('http://localhost:8081/bibliotecarios/reservas', {
        headers: {
          Authorization: `Bearer ${auth.token}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) throw new Error();
      const data = await response.json();
      setReservasList(data);
    } catch (err) {
      console.error('Erro ao carregar reservas:', err);
      setErrorMsg('Erro ao carregar as reservas.');
    } finally {
      setIsLoading(false);
    }
  };

  const cancelarReserva = async (reservaId) => {
    const confirmacao = await Swal.fire({
      title: "Cancelar reserva?",
      text: "Você tem certeza que deseja cancelar esta reserva?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, cancelar",
      cancelButtonText: "Não"
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(`http://localhost:8081/bibliotecarios/reservas/cancelar/${reservaId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${auth.token}` },
      });

      if (!res.ok) {
        const msg = await res.text();
        Swal.fire("Erro", msg || "Erro ao cancelar reserva.", "error");
        return;
      }

      Swal.fire("Cancelada!", "A reserva foi cancelada com sucesso.", "success");
      setReservasList((prev) => prev.filter((r) => r.reservaId !== reservaId));
    } catch (err) {
      console.error(err);
      Swal.fire("Erro", "Erro ao cancelar reserva.", "error");
    }
  };

  const confirmarRetirada = async (reserva) => {
    const confirmacao = await Swal.fire({
      title: "Confirmar retirada?",
      text: `Deseja confirmar a retirada do livro "${reserva.titulo}" para o aluno ${reserva.nome}?`,
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#2980b9",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, retirar",
      cancelButtonText: "Cancelar"
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(`http://localhost:8081/bibliotecarios/reservas/${reserva.reservaId}/confirmar-retirada`, {
        method: "POST",
        headers: { Authorization: `Bearer ${auth.token}` },
      });

      if (!res.ok) {
        let msg = "";
        try { msg = await res.text(); } catch {}
        Swal.fire("Erro", msg || "Erro ao confirmar retirada.", "error");
        return;
      }

      Swal.fire("Sucesso!", "Retirada confirmada com sucesso.", "success");
      setReservasList((prev) => prev.filter((r) => r.reservaId !== reserva.reservaId));
    } catch (err) {
      console.error(err);
      Swal.fire("Erro", "Erro ao confirmar retirada.", "error");
    }
  };

  useEffect(() => {
    if (auth?.token) carregarReservas();
  }, [auth]);

  const reservasFiltradas = reservasList.filter((reserva) => {
    const termo = busca.toLowerCase();
    return (
      (reserva.nome ?? '').toLowerCase().includes(termo) ||
      (reserva.titulo ?? '').toLowerCase().includes(termo)
    );
  });

  return (
    <main className="admin-reservas-wrapper">
      <h2>Gerenciamento de Reservas</h2>

      <input
        type="text"
        className="reserva-busca-input"
        placeholder="Buscar por aluno ou livro..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
      />

      {isLoading && <p className="text-center text-muted">Carregando reservas...</p>}
      {errorMsg && <p className="text-center text-danger">{errorMsg}</p>}

      {feedbackMsg && (
        <div
          className={`admin-reservas-alert alert ${
            feedbackMsg.tipo === 'erro' ? 'alert-danger' : 'alert-success'
          } text-center`}
        >
          {feedbackMsg.texto}
        </div>
      )}

      <div className="reservas-table-wrapper">
        <table className="reservas-table">
          <thead>
            <tr>
              <th>Aluno</th>
              <th>Nome</th>
              <th>Livro</th>
              <th>Data da Reserva</th>
              <th>Data de Vencimento</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {reservasFiltradas.length === 0 ? (
              <tr>
                <td colSpan="6" className="text-center">
                  Nenhuma reserva encontrada.
                </td>
              </tr>
            ) : (
              reservasFiltradas.map((reserva) => (
                <tr key={reserva.reservaId}>
                  <td>{reserva.matricula ?? 'N/A'}</td>
                  <td>{reserva.nome ?? 'Nome não disponível'}</td>
                  <td>{reserva.titulo ?? 'Livro não disponível'}</td>
                  <td>{formatDate(reserva.dataReserva)}</td>
                  <td>{formatDate(reserva.dataVencimento)}</td>
                  <td>
                    <div className="reservas-acoes">
                      <button
                        className="reservas-btn reservas-btn-cancelar"
                        onClick={() => cancelarReserva(reserva.reservaId)}
                      >
                        Cancelar
                      </button>
                      <button
                        className="reservas-btn reservas-btn-retirada"
                        onClick={() => confirmarRetirada(reserva)}
                      >
                        Confirmar Retirada
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </main>
  );
}
