import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../assets/css/perfil.css";
import { useAuth } from "../../authContext.jsx";

function Perfil() {
  const navigate = useNavigate();
  const { auth, logout } = useAuth();

  const [reservas, setReservas] = useState([]);
  const [emprestimos, setEmprestimos] = useState([]);
  const [comentarios, setComentarios] = useState([]);
  const [erro, setErro] = useState("");

  // Inicializa o formulário com os dados do auth (nome e email)
  const [userInfo, setUserInfo] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  useEffect(() => {
    if (!auth || !auth.matricula) return;

    // Inicializa userInfo com dados do auth quando carregar
    setUserInfo((prev) => ({
      ...prev,
      nome: auth.nome || "",
      email: auth.email || "",
    }));

    const headers = {
      Authorization: `Bearer ${auth.token}`,
    };

    async function safeJson(res) {
      if (!res.ok) return [];
      const text = await res.text();
      return text ? JSON.parse(text) : [];
    }

    const fetchDados = async () => {
      try {
        const [resEmprestimos, resReservas, resAvaliacoes] = await Promise.all([
          fetch(`http://localhost:8081/alunos/${auth.matricula}/emprestimos`, { headers }),
          fetch(`http://localhost:8081/alunos/${auth.matricula}/reservas`, { headers }),
          fetch(`http://localhost:8081/alunos/${auth.matricula}/avaliacoes`, { headers }),
        ]);

        const [emprestimosData, reservasData, avaliacoesData] = await Promise.all([
          safeJson(resEmprestimos),
          safeJson(resReservas),
          safeJson(resAvaliacoes),
        ]);

        setEmprestimos(emprestimosData);
        setReservas(reservasData);
        setComentarios(avaliacoesData);

        console.log("Reservas recebidas no front:", reservasData);
      } catch (err) {
        console.error("Erro ao carregar dados:", err);
        setErro("Erro ao carregar suas informações.");
      }
    };

    fetchDados();
  }, [auth]);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const handleSaveChanges = async (e) => {
    e.preventDefault();
    setErro("");

    if (!userInfo.nome || !userInfo.email || !userInfo.senha) {
      setErro("Todos os campos são obrigatórios.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8081/atualizar", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`, // se sua API exigir autorização para atualizar
        },
        body: JSON.stringify({
          matricula: auth.matricula,  // corrigido aqui: use auth.matricula
          nome: userInfo.nome,
          email: userInfo.email,
          senha: userInfo.senha,
        }),
      });

      if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || "Erro ao atualizar os dados.");
      }

      alert("Informações atualizadas com sucesso!");
      setUserInfo({ ...userInfo, senha: "" });
    } catch (err) {
      setErro(err.message);
    }
  };

  return (
    <>
      <nav className="menu-aluno">
        <ul>
          <li><a href="#reservas">Minhas Reservas</a></li>
          <li><a href="#emprestimos">Meus Emprestimos</a></li>
          <li><a href="#comentarios">Meus Comentários</a></li>
          <li><a href="#configuracoes">Configurações</a></li>
        </ul>
      </nav>

      <main className="perfil-conteudo">
        <section id="reservas" className="secao">
          <h2>Minhas Reservas</h2>
          {reservas.length === 0 ? (
            <p>Você ainda não fez nenhuma reserva.</p>
          ) : (
            reservas.map((reserva, index) => (
              <div key={index}>
                <p><strong>Livro:</strong> {reserva.titulo || "Sem título"}</p>
                <p><strong>Data da Reserva:</strong> {reserva.dataReserva ? new Date(reserva.dataReserva).toLocaleDateString('pt-BR') : 'Data não disponível'}</p>
                <p><strong>Data de Vencimento da Reserva:</strong> {reserva.dataVencimento ? new Date(reserva.dataVencimento).toLocaleDateString('pt-BR') : 'Data não disponível'}</p>
              </div>
            ))
          )}
        </section>

        <section id="emprestimos" className="secao">
          <h2>Meus Empréstimos</h2>
          {emprestimos.length === 0 ? (
            <p>Você ainda não fez nenhum empréstimo.</p>
          ) : (
            emprestimos.map((emprestimo, index) => (
              <div key={index}>
                <p><strong>Livro:</strong> {emprestimo.livro}</p>
                <p><strong>Data de Empréstimo:</strong> {emprestimo.dataEmprestimo}</p>
              </div>
            ))
          )}
        </section>

        <section id="comentarios" className="secao">
          <h2>Meus Comentários</h2>
          {comentarios.length === 0 ? (
            <p>Você ainda não comentou nenhum livro.</p>
          ) : (
            comentarios.map((comentario, index) => (
              <div key={index}>
                <p><strong>Livro:</strong> {comentario.livro?.titulo || comentario.livro || "Sem título"}</p>
                <p><strong>Nota:</strong> {comentario.nota}</p>
                <p><strong>Comentário:</strong> {comentario.comentario}</p>
              </div>
            ))
          )}
        </section>

        <section id="configuracoes" className="secao">
          <h2>Configurações</h2>
          {erro && <p style={{ color: "red" }}>{erro}</p>}
          <form onSubmit={handleSaveChanges}>
            <label htmlFor="nome">Nome:</label>
            <input
              type="text"
              id="nome"
              value={userInfo.nome}
              onChange={(e) =>
                setUserInfo({ ...userInfo, nome: e.target.value })
              }
            />

            <label htmlFor="email">E-mail:</label>
            <input
              type="email"
              id="email"
              value={userInfo.email}
              onChange={(e) =>
                setUserInfo({ ...userInfo, email: e.target.value })
              }
            />

            <label htmlFor="senha">Senha (obrigatória para salvar):</label>
            <input
              type="password"
              id="senha"
              placeholder="Digite sua senha"
              value={userInfo.senha}
              onChange={(e) =>
                setUserInfo({ ...userInfo, senha: e.target.value })
              }
            />

            <button type="submit">Salvar Alterações</button>
          </form>
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

export default Perfil;
