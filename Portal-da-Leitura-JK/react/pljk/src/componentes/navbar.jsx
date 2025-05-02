import React from "react";
import { Link } from "react-router-dom";
import "../style/navbar.css";

function Navbar() {
  return (
    <header>
      <nav className="logo cabecalho">
        <Link className="logo" to="/">
          <img
            src="/imagens/logo.png"
            width="150"
            height="50"
            alt="Logo"
          />
        </Link>
      </nav>
      <nav className="cabecalho">
        <div className="menu-colapsado" id="menuNavegacao">
          <ul className="lista-navegacao">
            <li className="Inicio">
              <Link className="link-principal" to="/">Início</Link>
            </li>
            <li className="Livros">
              <Link className="link-livros" to="/livros">Livros</Link>
            </li>
            <li className="Alunos">
              <Link className="link-aluno" to="/alunos/perfil">Área aluno</Link>
            </li>
          </ul>
        </div>

        <div className="notification-container">
          <i className="fas fa-bell notification-icon"></i>
          <span className="notification-badge"></span>
        </div>
      </nav>
    </header>
    
  );
}

export default Navbar;
