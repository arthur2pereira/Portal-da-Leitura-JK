import React, { useState, useEffect } from 'react';
import { useAuth } from '../../authContext.jsx';
import '../../assets/css/adminReserva.css';

function formatDateNoTimezone(dateString) {
  if (!dateString) return 'Data não disponível';
  const [year, month, day] = dateString.split('-');
  const date = new Date(year, month - 1, day);
  return date.toLocaleDateString('pt-BR');
}

export default function ReservasAdmin() {
  const { auth } = useAuth();
  const [reservasList, setReservasList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);
  const [feedbackMsg, setFeedbackMsg] = useState(null);

  const carregarReservas = async () => {
    if (!auth || !auth.token) return;

    try {
      const response = await fetch('http://localhost:8081/bibliotecarios/reservas', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${auth.token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) throw new Error();

      const data = await response.json();
      setReservasList(data);
      setIsLoading(false);
    } catch {
      setErrorMsg('Erro ao carregar as reservas.');
      setIsLoading(false);
    }
  };


  const cancelarReserva = async (reservaId) => {
    try {
      const response = await fetch(`http://localhost:8081/bibliotecarios/reservas/cancelar/${reservaId}`, {
        method: 'DELETE',
        headers: {
          ...(auth?.token && { Authorization: `Bearer ${auth.token}` }),
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) throw new Error();

      await carregarReservas();
      setFeedbackMsg({ tipo: 'sucesso', texto: 'Reserva cancelada com sucesso' });
    } catch {
      setFeedbackMsg({ tipo: 'erro', texto: 'Erro ao cancelar a reserva' });
    }
  };

  useEffect(() => {
    if (auth && auth.token) {
      carregarReservas();
    } 
  }, [auth]);


  if (isLoading) return <p className="loading-text">Carregando reservas...</p>;
  if (errorMsg) return <p className="error-text">{errorMsg}</p>;

  return (
    <div className="reservas-admin-container">
      <h1 className="reservas-admin-title">Gerenciamento de Reservas</h1>

      {feedbackMsg && (
        <div className={`feedback-message ${feedbackMsg.tipo === 'erro' ? 'error' : 'success'}`}>
          {feedbackMsg.texto}
        </div>
      )}

      <table className="reservas-table">
        <thead>
          <tr>
            <th className="table-header">Aluno</th>
            <th className="table-header">Livro</th>
            <th className="table-header">Data da Reserva</th>
            <th className="table-header">Data de Vencimento</th>
            <th className="table-header">Ações</th>
          </tr>
        </thead>
          <tbody>
            {reservasList.length === 0 ? (
              <tr>
                <td colSpan="5" className="table-cell" style={{ textAlign: 'center' }}>
                  Nenhuma reserva encontrada.
                </td>
              </tr>
            ) : (
              reservasList.map((reserva) => (
                <tr key={reserva.reservaId} className="table-row">
                  <td className="table-cell">{reserva.matricula ?? 'Aluno não disponível'}</td>
                  <td className="table-cell">{reserva.titulo ?? 'Livro não disponível'}</td>
                  <td className="table-cell">
                    {reserva.dataReserva
                      ? formatDateNoTimezone(reserva.dataReserva)
                      : 'Data não disponível'}
                  </td>
                  <td className="table-cell">
                    {reserva.dataVencimento
                      ? formatDateNoTimezone(reserva.dataVencimento)
                      : 'Data não disponível'}
                  </td>
                  <td className="table-cell">
                    <button className="cancel-button" onClick={() => cancelarReserva(reserva.reservaId)}>
                      Cancelar
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
      </table>
    </div>
  );
}
