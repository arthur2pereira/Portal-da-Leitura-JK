import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../assets/css/perfilAdmin.css";

function AdminArea() {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [adminInfo, setAdminInfo] = useState({
    nome: "",
    email: ""
  });

  useEffect(() => {
    const authData = localStorage.getItem("auth");
    if (authData) {
      const user = JSON.parse(authData);
      if (user.tipo === "bibliotecario") {
        setIsAuthenticated(true);
        setAdminInfo({
          nome: user.nome || "",
          email: user.email || ""
        });
      } else {
        navigate("/");
      }
    } else {
      navigate("/");
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("auth");
    setIsAuthenticated(false);
    navigate("/");
  };

  const irPara = (rota) => {
    navigate(rota);
  };

  return (
    <>
      <main className="perfil-conteudo">
        <section id="painel" className="secao">
          <h2>Painel do Administrador</h2>
            <button onClick={() => irPara("/admin/livros")}>Gerenciar Livros</button>
            <button onClick={() => irPara("/admin/alunos")}>Gerenciar Alunos</button>
            <button onClick={() => irPara("/admin/reservas")}>Ver Reservas</button>
            <button onClick={() => irPara("/admin/notificacoes")}>Notificações</button>
        </section>

        {isAuthenticated && (
          <button className="btn-logout" onClick={handleLogout}>
            Sair da conta
          </button>
        )}
      </main>
    </>
  );
}

export default AdminArea;
