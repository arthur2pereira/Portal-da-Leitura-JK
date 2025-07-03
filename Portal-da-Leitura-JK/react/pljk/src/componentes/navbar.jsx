import React, { useEffect, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
import { HashLink } from "react-router-hash-link"
import { useAuth } from "../authContext"
import "../style/navbar.css"

function Navbar() {
  const location = useLocation()
  const navigate = useNavigate()
  const { auth } = useAuth()
  const isAuthenticated = !!auth
  const tipo = auth?.tipo
  const matricula = auth?.matricula

  const [temNotificacao, setTemNotificacao] = useState(false)

  const isActive = (path) => (location.pathname === path ? "ativo" : "")

  const handleNotificacaoClick = () => {
    if (tipo === "aluno") navigate("/aluno/notificacoes")
    else if (tipo === "bibliotecario") navigate("/admin/notificacoes")
  }

  useEffect(() => {
    const buscarNotificacoesNaoLidas = async () => {
      const token = localStorage.getItem("token")
      try {
        let url = null

        if (tipo === "aluno" && matricula) {
          url = `http://localhost:8081/notificacoes/aluno/${matricula}/nao-lidas`
        } else if (tipo === "bibliotecario" && auth?.bibliotecarioId) {
          url = `http://localhost:8081/notificacoes/bibliotecario/${auth.bibliotecarioId}/nao-lidas`
        }

        if (url) {
          const response = await fetch(url, {
            headers: { Authorization: `Bearer ${token}` },
          })
          setTemNotificacao(response.ok)
        } else {
          setTemNotificacao(false)
        }
      } catch (error) {
        console.error("Erro ao verificar notificações:", error)
        setTemNotificacao(false)
      }
    }

    if (isAuthenticated) {
      buscarNotificacoesNaoLidas()
    }
  }, [tipo, matricula, auth, isAuthenticated])

  return (
    <header>
      <nav className="navbar-custom">
        {/* Logo à esquerda */}
        <div className="navbar-logo">
          <Link to="/">
            <img src="/imagens/logo.png" width="150" height="50" alt="Logo" />
          </Link>
        </div>

        {/* Links centrais — Livros e Sobre */}
        <ul className="navbar-links">
          <li className={isActive("/Catalogo") || isActive("/livros")}>
            <Link className="link-livros" to="/Catalogo">
              Livros
            </Link>
          </li>
          <li className={isActive("/")}>
            <HashLink smooth to="/#sobre" className="link-sobre">
              Sobre
            </HashLink>
          </li>
        </ul>

        {/* Área direita */}
        <div className="navbar-botoes">
          {!isAuthenticated ? (
            <>
              <Link className="link-login" to="/login">
                Entrar
              </Link>
              <Link className="link-cadastro" to="/cadastro">
                Cadastro
              </Link>
            </>
          ) : (
            <>
              {/* Link área aluno ou área admin antes do sino */}
              {tipo === "aluno" && (
                <Link className="link-aluno" to="/aluno/perfil" style={{ marginRight: "15px" }}>
                  Área aluno
                </Link>
              )}
              {tipo === "bibliotecario" && (
                <Link className="link-admin" to="/admin/area" style={{ marginRight: "15px" }}>
                  Área admin
                </Link>
              )}

              {/* Sino de notificação */}
              <div
                className="notification-container"
                onClick={handleNotificacaoClick}
                style={{ cursor: "pointer" }}
              >
                <i className="fas fa-bell notification-icon"></i>
                {temNotificacao && <span className="notification-badge"></span>}
              </div>
            </>
          )}
        </div>
      </nav>
    </header>
  )
}

export default Navbar
