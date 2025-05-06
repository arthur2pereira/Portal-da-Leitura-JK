import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../authContext.jsx";
import "../../assets/css/perfilAdmin.css";

function AdminArea() {
  const navigate = useNavigate();
  const { auth, logout } = useAuth();
  const [adminInfo, setAdminInfo] = useState({
    nome: "",
    email: ""
  });

  useEffect(() => {
    if (auth && auth.tipo === "bibliotecario") {
      setAdminInfo({
        nome: auth.nome || "",
        email: auth.email || ""
      });
    } else {
      navigate("/");
    }
  }, [auth, navigate]);

  const handleLogout = () => {
    logout();
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

        {auth && (
          <button className="btn-logout" onClick={handleLogout}>
            Sair da conta
          </button>
        )}
      </main>
    </>
  );
}

export default AdminArea;
