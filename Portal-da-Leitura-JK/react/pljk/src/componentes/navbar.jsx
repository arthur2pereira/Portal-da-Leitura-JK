import React, { useEffect, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
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

  const isActive = (path) => location.pathname === path ? "ativo" : ""

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
        } else if (tipo === "bibliotecario" && auth?.email) {
          url = `http://localhost:8081/notificacoes/bibliotecario/${auth.email}/nao-lidas`
        }
  
        if (url) {
          const response = await fetch(url, {
            headers: { Authorization: `Bearer ${token}` }
          })
          setTemNotificacao(response.ok)
        }
      } catch (error) {
        console.error("Erro ao verificar notificações:", error)
      }
    }
  
    buscarNotificacoesNaoLidas()
  }, [tipo, matricula, auth])  

  return (
    <header>
      <nav className="logo cabecalho">
        <Link className="logo" to="/">
          <img src="/imagens/logo.png" width="150" height="50" alt="Logo" />
        </Link>
      </nav>

      <nav className="cabecalho">
        <div className="menu-colapsado" id="menuNavegacao">
          <ul className="lista-navegacao">

            {/* Sempre visível */}
            <li className={`Livros ${isActive("/Catalogo") || isActive("/livros")}`}>
              <Link className="link-livros" to="/Catalogo">Livros</Link>
            </li>

            {tipo === "aluno" && (
              <>
                <li className={`Inicio ${isActive("/")}`}>
                  <Link className="link-principal" to="/">Início</Link>
                </li>
                <li className={`Alunos ${isActive("/aluno/perfil")}`}>
                  <Link className="link-aluno" to="/aluno/perfil">Área aluno</Link>
                </li>
              </>
            )}

            {tipo === "bibliotecario" && (
              <li className={`Admin ${isActive("/admin/area")}`}>
                <Link className="link-admin" to="/admin/area">Área admin</Link>
              </li>
            )}

            {!isAuthenticated && (
              <>
                <li className={`Login ${isActive("/login")}`}>
                  <Link className="link-login" to="/login">Login</Link>
                </li>
                <li className={`Cadastro ${isActive("/cadastro")}`}>
                  <Link className="link-cadastro" to="/cadastro">Cadastro</Link>
                </li>
              </>
            )}
          </ul>
        </div>

        {isAuthenticated && (
          <div className="notification-container" onClick={handleNotificacaoClick} style={{ cursor: "pointer" }}>
            <i className="fas fa-bell notification-icon"></i>
            {temNotificacao && <span className="notification-badge"></span>}
          </div>
        )}
      </nav>
    </header>
  )
}

export default Navbar
