import React, { useState, useEffect } from 'react';
import { useAuth } from '../../authContext.jsx';
import '../../assets/css/adminReserva.css';

export default function ReservasAdmin() {
  const { auth } = useAuth();
  const [reservasList, setReservasList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);
  const [feedbackMsg, setFeedbackMsg] = useState(null);

  const carregarReservas = async () => {
    try {
      const response = await fetch('http://localhost:8081/bibliotecarios/reservas', {
        method: 'GET',
        headers: {
          ...(auth?.token && { Authorization: `Bearer ${auth.token}` }),
          "Content-Type": "application/json"
        }        
      });

      if (!response.ok) {
        throw new Error('Erro ao carregar as reservas');
      }

      const data = await response.json();
      setReservasList(data);
      setIsLoading(false);
    } catch (error) {
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
          "Content-Type": "application/json"
        },
      });
  
      if (!response.ok) {
        throw new Error('Erro ao cancelar a reserva');
      }
  
      await carregarReservas();
      setFeedbackMsg({ tipo: 'sucesso', texto: 'Reserva cancelada com sucesso' });
    } catch (error) {
      setFeedbackMsg({ tipo: 'erro', texto: 'Erro ao cancelar a reserva' });
    }
  };
  
  useEffect(() => {
    console.log("Auth atual:", auth);
    if (auth?.token) {
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
            <th className="table-header">Ações</th>
          </tr>
        </thead>
        <tbody>
          {reservasList.map((reserva) => (
            <tr key={reserva.id} className="table-row">
              <td className="table-cell">{reserva.aluno.nome}</td>
              <td className="table-cell">{reserva.livro.titulo}</td>
              <td className="table-cell">{new Date(reserva.dataReserva).toLocaleString()}</td>
              <td className="table-cell">
                <button
                  className="cancel-button"
                  onClick={() => cancelarReserva(reserva.id)}
                >
                  Cancelar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
