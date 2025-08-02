import React, { useState, useEffect } from 'react'
import { useAuth } from '../../authContext.jsx'
import '../../assets/css/adminReserva.css'

function formatDate(dateString) {
  if (!dateString) return 'Data não disponível'
  const [year, month, day] = dateString.split('-')
  return new Date(year, month - 1, day).toLocaleDateString('pt-BR')
}

export default function ReservasAdmin() {
  const { auth } = useAuth()
  const [reservasList, setReservasList] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [errorMsg, setErrorMsg] = useState(null)
  const [feedbackMsg, setFeedbackMsg] = useState(null)
  const [busca, setBusca] = useState('')

  const carregarReservas = async () => {
    if (!auth?.token) return
    try {
      const response = await fetch('http://localhost:8081/bibliotecarios/reservas', {
        headers: {
          Authorization: `Bearer ${auth.token}`,
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) throw new Error()
      const data = await response.json()
      setReservasList(data)
    } catch {
      setErrorMsg('Erro ao carregar as reservas.')
    } finally {
      setIsLoading(false)
    }
  }

  const cancelarReserva = async (reservaId) => {
    try {
      const response = await fetch(`http://localhost:8081/bibliotecarios/reservas/cancelar/${reservaId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${auth.token}`,
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) throw new Error()
      await carregarReservas()
      setFeedbackMsg({ tipo: 'sucesso', texto: 'Reserva cancelada com sucesso' })
    } catch {
      setFeedbackMsg({ tipo: 'erro', texto: 'Erro ao cancelar a reserva' })
    }
  }

  const confirmarRetirada = async (reservaId) => {
    try {
      const response = await fetch(`http://localhost:8081/bibliotecarios/reservas/${reservaId}/confirmar-retirada`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${auth.token}`,
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) throw new Error()
      await carregarReservas()
      setFeedbackMsg({ tipo: 'sucesso', texto: 'Retirada confirmada com sucesso' })
    } catch {
      setFeedbackMsg({ tipo: 'erro', texto: 'Erro ao confirmar retirada' })
    }
  }

  useEffect(() => {
    if (auth?.token) carregarReservas()
  }, [auth])

  const reservasFiltradas = reservasList.filter((reserva) => {
    const termo = busca.toLowerCase()
    return (
      (reserva.nome ?? '').toLowerCase().includes(termo) ||
      (reserva.titulo ?? '').toLowerCase().includes(termo)
    )
  })

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
        <div className={`admin-reservas-alert alert ${feedbackMsg.tipo === 'erro' ? 'alert-danger' : 'alert-success'} text-center`}>
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
                <td colSpan="6" className="text-center">Nenhuma reserva encontrada.</td>
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
                        onClick={() => confirmarRetirada(reserva.reservaId)}
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
  )
}
