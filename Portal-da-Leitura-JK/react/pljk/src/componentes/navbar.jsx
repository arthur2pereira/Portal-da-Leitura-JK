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
  const [menuAberto, setMenuAberto] = useState(false)
  const [seccaoAtiva, setSeccaoAtiva] = useState("")

  useEffect(() => {
    function onScroll() {
      const scrollY = window.scrollY
      const sobreElement = document.getElementById("sobre")
      if (sobreElement) {
        const topSobre = sobreElement.offsetTop
        const bottomSobre = topSobre + sobreElement.offsetHeight

        if (scrollY + 80 >= topSobre && scrollY + 80 < bottomSobre) {
          setSeccaoAtiva("sobre")
          return
        }
      }
      setSeccaoAtiva("")
    }

    window.addEventListener("scroll", onScroll)
    onScroll() // para inicializar corretamente
    return () => window.removeEventListener("scroll", onScroll)
  }, [])

  const isActive = (path, secao) => {
    if (secao === "sobre") {
      return seccaoAtiva === "sobre" && location.pathname === "/" ? "ativo" : ""
    }
    return location.pathname === path ? "ativo" : ""
  }

  const handleNotificacaoClick = () => {
    if (tipo === "aluno") navigate("/aluno/notificacoes")
    else if (tipo === "bibliotecario") navigate("/admin/notificacoes")
    setMenuAberto(false)
  }

  const toggleMenu = () => {
    setMenuAberto(!menuAberto)
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
        <div className="navbar-logo">
          <Link to="/" onClick={() => { setMenuAberto(false); setSeccaoAtiva("") }}>
            <img src="/imagens/logo.png" width="150" height="50" alt="Logo" />
          </Link>
        </div>

        <ul className="navbar-links">
          <li className={isActive("/Catalogo") || isActive("/livros")}>
            <Link to="/Catalogo">Livros</Link>
          </li>
          <li className={isActive("/", "sobre")}>
            <HashLink smooth to="/#sobre">
              Sobre
            </HashLink>
          </li>
        </ul>

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
              {tipo === "aluno" && (
                <Link className="link-aluno" to="/aluno/perfil">
                  Área aluno
                </Link>
              )}
              {tipo === "bibliotecario" && (
                <Link className="link-admin" to="/admin/area">
                  Área admin
                </Link>
              )}

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

        <div className="navbar-toggle" onClick={toggleMenu} aria-label="Menu">
          <i className={menuAberto ? "fas fa-times" : "fas fa-bars"}></i>
        </div>

        <div className={`navbar-mobile-menu ${menuAberto ? "active" : ""}`}>
          <ul className="navbar-mobile-links">
          <li className={isActive("/Catalogo") || isActive("/livros") ? "ativo" : ""}>
            <Link to="/Catalogo" onClick={() => setMenuAberto(false)}>
              Livros
            </Link>
          </li>
          <li className={isActive("/", "sobre") ? "ativo" : ""}>
            <HashLink smooth to="/#sobre" onClick={() => setMenuAberto(false)}>
              Sobre
            </HashLink>
          </li>

          {!isAuthenticated ? (
            <>
              <li>
                <Link to="/login" onClick={() => setMenuAberto(false)}>
                  Entrar
                </Link>
              </li>
              <li>
                <Link to="/cadastro" onClick={() => setMenuAberto(false)}>
                  Cadastro
                </Link>
              </li>
            </>
          ) : (
            <>
              <li className={isActive(tipo === "aluno" ? "/aluno/perfil" : "/admin/area") ? "ativo" : ""}>
                <Link to={tipo === "aluno" ? "/aluno/perfil" : "/admin/area"} onClick={() => setMenuAberto(false)}>
                  {tipo === "aluno" ? "Área aluno" : "Área admin"}
                </Link>
              </li>
              <li className={isActive(tipo === "aluno" ? "/aluno/notificacoes" : "/admin/notificacoes") ? "ativo" : ""}>
                <Link to={tipo === "aluno" ? "/aluno/notificacoes" : "/admin/notificacoes"} onClick={() => setMenuAberto(false)}>
                  Notificações
                </Link>
              </li>
            </>
          )}
        </ul>
        </div>
      </nav>
    </header>
  )
}

export default Navbar
