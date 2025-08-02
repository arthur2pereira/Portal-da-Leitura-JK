import React, { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useAuth } from "../../authContext.jsx"
import { Book, Users, Bell, FileText, LogOut, Repeat } from "lucide-react"
import "bootstrap/dist/css/bootstrap.min.css"
import "../../assets/css/adminArea.css"

function AdminArea() {
  const navigate = useNavigate()
  const { auth, logout } = useAuth()
  const [adminInfo, setAdminInfo] = useState({ nome: "", email: "" })

  useEffect(() => {
    if (auth?.tipo === "bibliotecario") {
      setAdminInfo({ nome: auth.nome || "", email: auth.email || "" })
    } else {
      navigate("/")
    }
  }, [auth, navigate])

  const handleLogout = () => {
    logout()
    navigate("/")
  }

  const cards = [
    {
      icon: <Book className="text-success me-2" />,
      title: "Livros",
      description: "Gerencie os livros do acervo.",
      rota: "/admin/livros"
    },
    {
      icon: <Users className="text-success me-2" />,
      title: "Alunos",
      description: "Gerencie os alunos cadastrados.",
      rota: "/admin/alunos"
    },
    {
      icon: <Repeat className="text-success me-2" />,
      title: "Reservas",
      description: "Controle de reservas em andamento.",
      rota: "/admin/reservas"
    },
    {
      icon: <FileText className="text-success me-2" />,
      title: "Empréstimos",
      description: "Gerencie os empréstimos ativos.",
      rota: "/admin/emprestimos"
    },
    {
      icon: <Bell className="text-success me-2" />,
      title: "Notificações",
      description: "Envie e visualize notificações.",
      rota: "/admin/notificacoes"
    },
    {
      icon: <FileText className="text-success me-2" />,
      title: "Penalidades",
      description: "Visualize e gerencie penalidades dos alunos.",
      rota: "/admin/penalidades"
    }
  ]

  return (
    <main className="admin-area-wrapper">
      <div className="container py-5">
        <h2 className="mb-4 text-success fw-bold text-center">Painel do Administrador</h2>

        <div className="row g-4">
          {cards.map((card, index) => (
            <div key={index} className="col-md-6 col-lg-4">
              <div
                className="card h-100 shadow-sm border-0 card-hover"
                role="button"
                onClick={() => navigate(card.rota)}
              >
                <div className="card-body">
                  <div className="d-flex align-items-center mb-2">
                    {card.icon}
                    <h5 className="card-title m-0 text-success">{card.title}</h5>
                  </div>
                  <p className="card-text text-muted">{card.description}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '40px' }}>
          <button className="btn btn-danger d-flex align-items-center gap-2" onClick={handleLogout}>
            <LogOut size={18} /> Sair da conta
         </button>
        </div> 
      </div>
    </main>
  )
}

export default AdminArea
