import React, { useEffect, useState, useCallback } from "react";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import "../../assets/css/perfil.css";
import { LogOut } from "lucide-react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useAuth } from "../../authContext.jsx";

function formatDateNoTimezone(dateString) {
  if (!dateString) return "Data não disponível";
  const [year, month, day] = dateString.split("-");
  const date = new Date(year, month - 1, day);
  return date.toLocaleDateString("pt-BR");
}

function Perfil() {
  const navigate = useNavigate();
  const { auth, logout } = useAuth();

  const [reservas, setReservas] = useState([]);
  const [emprestimos, setEmprestimos] = useState([]);
  const [comentarios, setComentarios] = useState([]);
  const [erro, setErro] = useState("");

  const [userInfo, setUserInfo] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  const safeJson = async (res) => {
    if (!res.ok) return [];
    const text = await res.text();
    return text ? JSON.parse(text) : [];
  };

  useEffect(() => {
    if (!auth?.matricula || !auth?.token) return;

    setUserInfo((prev) => ({
      ...prev,
      nome: auth?.nome || "",
      email: auth?.email || "",
    }));

    const headers = auth?.token
      ? { Authorization: `Bearer ${auth.token}` }
      : {};

    const fetchDados = async () => {
      try {
        const [resEmprestimos, resReservas, resAvaliacoes] = await Promise.all([
          fetch(`http://localhost:8081/alunos/${auth?.matricula}/emprestimos`, { headers }),
          fetch(`http://localhost:8081/alunos/${auth?.matricula}/reservas`, { headers }),
          fetch(`http://localhost:8081/alunos/${auth?.matricula}/avaliacoes`, { headers }),
        ]);

        const [emprestimosData, reservasData, avaliacoesData] = await Promise.all([
          safeJson(resEmprestimos),
          safeJson(resReservas),
          safeJson(resAvaliacoes),
        ]);

        setEmprestimos(emprestimosData);
        setReservas(reservasData);
        setComentarios(avaliacoesData);
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

  const handleDesativarConta = async () => {
    if (reservas.length > 0 || emprestimos.length > 0) {
      Swal.fire("Atenção", "Você não pode desativar a conta com reservas ou empréstimos ativos.", "warning");
      return;
    }

    const { isConfirmed: aceitouTermos } = await Swal.fire({
      title: "Termos de Desativação",
      html: `
        <p>Ao desativar sua conta, você concorda com os seguintes termos:</p>
        <ul>
          <li>Sua conta ficará temporariamente inativa.</li>
          <li>Você não poderá acessar ou utilizar os serviços enquanto a conta estiver desativada.</li>
          <li>Todas as suas informações, histórico de reservas, empréstimos e avaliações serão preservadas.</li>
          <li>A conta pode ser reativada a qualquer momento mediante confirmação da sua identidade.</li>
          <li>Será necessário fornecer sua senha atual para confirmar a desativação.</li>
        </ul>
        <p>Ao clicar em “Concordo”, você confirma que leu e aceita este termo.</p>
        <div style="text-align:left; color:#005c5a; font-weight:500;">
          <input type="checkbox" id="swal-termos" style="accent-color:#005c5a;">
          Li e concordo com os termos
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: "Continuar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#3ea66b",
      cancelButtonColor: "#005c5a",
      preConfirm: () => {
        const checkbox = Swal.getPopup().querySelector("#swal-termos");
        if (!checkbox.checked) {
          Swal.showValidationMessage("Você precisa aceitar os termos para continuar.");
        }
        return checkbox.checked;
      },
      allowOutsideClick: false,
      allowEscapeKey: false,
    });

    if (!aceitouTermos) return;

    const { value: senha } = await Swal.fire({
      title: "Confirmar desativação",
      input: "password",
      inputPlaceholder: "Digite sua senha",
      showCancelButton: true,
      confirmButtonText: "Desativar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#d33",
      inputValidator: (value) => !value && "Digite sua senha"
    });

    if (!senha) return;

    try {
      const res = await fetch(`http://localhost:8081/alunos/desativar/${auth.matricula}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`
        },
        body: JSON.stringify({ senha })
      });

      if (res.ok) {
        await Swal.fire("Conta desativada", "Sua conta foi desativada com sucesso.", "success");
        logout();
        navigate("/");
      } else {
        const msg = await res.text();
        Swal.fire("Erro", msg || "Não foi possível desativar a conta.", "error");
      }
    } catch (err) {
      Swal.fire("Erro", "Ocorreu um erro ao desativar a conta.", "error");
    }
  };

  const handleExcluirConta = async () => {
    if (reservas.length > 0 || emprestimos.length > 0) {
      Swal.fire("Atenção", "Você não pode excluir a conta com reservas ou empréstimos ativos.", "warning");
      return;
    }

    const { isConfirmed: aceitouTermos } = await Swal.fire({
      title: "Termos de Exclusão",
      html: `
        <p>Ao excluir sua conta, você concorda com os seguintes termos:</p>
        <ul>
          <li>A exclusão é permanente, mas sua conta ficará em estado de exclusão pendente por 30 dias, durante os quais você ainda poderá reativá-la.</li>
          <li>Após esse período, todos os dados pessoais e registros relacionados à sua conta serão permanentemente removidos ou anonimizados, e não poderão ser recuperados.</li>
          <li>Você deve confirmar sua senha e aceitar este termo para concluir o processo de exclusão.</li>
          <li>Durante o período de exclusão pendente, você não poderá acessar ou utilizar os serviços normalmente.</li>
        </ul>
        <p>Ao clicar em “Concordo”, você confirma que leu e aceita este termo.</p>
        <div style="text-align:left; color:#005c5a; font-weight:500;">
          <input type="checkbox" id="swal-termos" style="accent-color:#005c5a;">
          Li e concordo com os termos
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: "Continuar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#3ea66b",
      cancelButtonColor: "#005c5a",
      preConfirm: () => {
        const checkbox = Swal.getPopup().querySelector("#swal-termos");
        if (!checkbox.checked) {
          Swal.showValidationMessage("Você precisa aceitar os termos para continuar.");
        }
        return checkbox.checked;
      },
      allowOutsideClick: false,
      allowEscapeKey: false,
    });

    if (!aceitouTermos) return;

    const { value: senha } = await Swal.fire({
      title: "Confirme sua senha",
      input: "password",
      inputPlaceholder: "Digite sua senha",
      showCancelButton: true,
      confirmButtonText: "Excluir",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#d33",
      inputValidator: (value) => !value && "Digite sua senha"
    });

    if (!senha) return;

    try {
      const res = await fetch(`http://localhost:8081/alunos/excluir/${auth.matricula}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`
        },
        body: JSON.stringify({ senha })
      });

      if (res.ok) {
        await Swal.fire("Conta marcada para exclusão", "Você tem 30 dias para reativar, caso queira.", "success");
        logout();
        navigate("/");
      } else {
        const msg = await res.text();
        Swal.fire("Erro", msg || "Não foi possível marcar a conta para exclusão.", "error");
      }
    } catch (err) {
      Swal.fire("Erro", "Ocorreu um erro ao excluir a conta.", "error");
    }
  };

  const handleEditarAvaliacao = async (comentario) => {
    let notaSelecionada = comentario.nota || 0;
    let textoComentario = comentario.comentario || "";

    const { value: result } = await Swal.fire({
      title: "Editar Avaliação",
      html: `
        <div style="text-align:center; margin-bottom:10px;">
          ${[1,2,3,4,5].map(i => `
            <span class="swal-star" data-value="${i}" style="font-size:1.5rem; cursor:pointer; color:${i <= notaSelecionada ? 'gold' : '#ccc'};">★</span>
          `).join('')}
        </div>
        <textarea id="swal-comentario" class="swal2-textarea" placeholder="Comentário (opcional)">${textoComentario}</textarea>
      `,
      showCancelButton: true,
      confirmButtonText: "Salvar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#4caf50",
      preConfirm: () => {
        const comentarioTexto = document.getElementById("swal-comentario").value.trim();
        if (notaSelecionada < 1 || notaSelecionada > 5) {
          Swal.showValidationMessage("Selecione uma nota entre 1 e 5 estrelas");
          return false;
        }
        return { nota: notaSelecionada, comentario: comentarioTexto };
      },
      didOpen: () => {
        const stars = Swal.getPopup().querySelectorAll(".swal-star");
        stars.forEach(star => {
          star.addEventListener("click", () => {
            notaSelecionada = parseInt(star.dataset.value);
            stars.forEach((s, idx) => {
              s.style.color = idx < notaSelecionada ? "gold" : "#ccc";
            });
          });
        });
      }
    });

    if (!result) return;

    try {
      const resComentario = await fetch(
        `http://localhost:8081/avaliacoes/${comentario.avaliacaoId}/editar-comentario?novoComentario=${encodeURIComponent(result.comentario)}`,
        {
          method: "PUT",
          headers: { Authorization: `Bearer ${auth.token}` },
        }
      );

      if (!resComentario.ok) throw new Error("Não foi possível atualizar o comentário.");

      const resNota = await fetch(
        `http://localhost:8081/avaliacoes/${comentario.avaliacaoId}/alterar-nota?novaNota=${result.nota}`,
        {
          method: "PUT",
          headers: { Authorization: `Bearer ${auth.token}` },
        }
      );

      if (!resNota.ok) throw new Error("Não foi possível atualizar a nota.");

      Swal.fire("Atualizado!", "Sua avaliação foi atualizada com sucesso.", "success");
      setComentarios(prev =>
        prev.map(c =>
          c.avaliacaoId === comentario.avaliacaoId
            ? { ...c, nota: result.nota, comentario: result.comentario }
            : c
        )
      );

    } catch (err) {
      Swal.fire("Erro", err.message, "error");
    }
  };

  const handleExcluirAvaliacao = async (avaliacaoId) => {
    const confirmacao = await Swal.fire({
      title: 'Tem certeza?',
      text: "Você não poderá reverter esta ação!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#f44336', // vermelho
      cancelButtonColor: '#6c757d', 
      confirmButtonText: 'Sim, excluir',
      cancelButtonText: 'Cancelar'
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(`http://localhost:8081/avaliacoes/${avaliacaoId}/excluir`, {
        method: 'DELETE',
        headers: auth?.token ? { Authorization: `Bearer ${auth.token}` } : {},
      });

      if (res.ok) {
        Swal.fire('Excluído!', 'Seu comentário foi removido.', 'success');
        setComentarios((prev) => prev.filter((c) => c.avaliacaoId !== avaliacaoId));
      } else {
        const msg = await res.text();
        Swal.fire('Erro', msg || 'Não foi possível excluir o comentário.', 'error');
      }
    } catch (err) {
      console.error(err);
      Swal.fire('Erro', 'Ocorreu um erro ao excluir o comentário.', 'error');
    }
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
          ...(auth?.token && { Authorization: `Bearer ${auth.token}` }),
        },
        body: JSON.stringify({
          matricula: auth?.matricula,
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

  const handleCancelarReserva = useCallback(async (reservaId) => {
    const confirmacao = await Swal.fire({
      title: "Cancelar reserva?",
      text: "Você tem certeza que deseja cancelar esta reserva?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, cancelar",
      cancelButtonText: "Não",
    });

    if (!confirmacao.isConfirmed) return;

    try {
      const res = await fetch(`http://localhost:8081/reservas/${reservaId}/cancelar`, {
        method: "DELETE",
        headers: auth?.token ? { Authorization: `Bearer ${auth.token}` } : {},
      });
      if (res.ok) {
        Swal.fire("Cancelada!", "A reserva foi cancelada com sucesso.", "success");
        setReservas((prev) => prev.filter((r) => r.reservaId !== reservaId));
      } else {
        const msg = await res.text();
        Swal.fire("Erro", msg || "Erro ao cancelar reserva.", "error");
      }
    } catch (err) {
      console.error(err);
      Swal.fire("Erro", "Erro ao cancelar reserva.", "error");
    }
  }, [auth?.token]);

  const handleRenovarEmprestimo = useCallback(async (emprestimoId) => {
    try {
      const res = await fetch(
        `http://localhost:8081/emprestimos/${emprestimoId}/renovar?matricula=${auth?.matricula}`,
        {
          method: "PUT",
          headers: auth?.token ? { Authorization: `Bearer ${auth.token}` } : {},
        }
      );
      if (res.ok) {
        alert("Prazo renovado por +7 dias!");
        setEmprestimos((prev) =>
          prev.map((e) =>
            e.emprestimoId === emprestimoId ? { ...e, renovado: true } : e
          )
        );
      } else {
        const msg = await res.text();
        alert(msg || "Erro ao renovar empréstimo.");
      }
    } catch (err) {
      console.error(err);
      alert("Erro ao renovar empréstimo.");
    }
  }, [auth?.matricula, auth?.token]);

  if (!auth) {
    return (
      <main className="perfil-conteudo">
        <p>Carregando...</p>
      </main>
    );
  }

  return (
    <>
      <nav className="menu-aluno">
        <ul>
          <li><a href="#reservas">Minhas Reservas</a></li>
          <li><a href="#emprestimos">Meus Empréstimos</a></li>
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
          reservas.map((reserva) => (
            <div key={reserva.reservaId} className="card-principal">
              <p><strong>Livro:</strong> {reserva.titulo || "Sem título"}</p>
              <p><strong>Data da Reserva:</strong> {formatDateNoTimezone(reserva.dataReserva)}</p>
              <p><strong>Data de Vencimento:</strong> {formatDateNoTimezone(reserva.dataVencimento)}</p>
              <button
                className="perfil-btn-cancelar-reserva"
                onClick={() => handleCancelarReserva(reserva.reservaId)}
              >
                Cancelar Reserva
              </button>
            </div>
          ))
        )}
      </section>

      <section id="emprestimos" className="secao">
        <h2>Meus Empréstimos</h2>
        {emprestimos.length === 0 ? (
          <p>Você ainda não fez nenhum empréstimo.</p>
        ) : (
          emprestimos.map((emprestimo) => (
            <div key={emprestimo.emprestimoId} className="card-principal">
              <p><strong>Livro:</strong> {emprestimo.titulo}</p>
              <p><strong>Data de Empréstimo:</strong> {formatDateNoTimezone(emprestimo.dataEmprestimo)}</p>
              <p><strong>Data de Vencimento:</strong> {formatDateNoTimezone(emprestimo.dataVencimento)}</p>
              {!emprestimo.renovado && (
                <button
                  className="perfil-btn-renovar-emprestimo"
                  onClick={() => handleRenovarEmprestimo(emprestimo.emprestimoId)}
                >
                  Renovar Empréstimo
                </button>
              )}
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
            <div key={index} className="comentario-card">
              <h4>{comentario.titulo || "Sem título"}</h4>
              {"★".repeat(comentario.nota)}{"☆".repeat(5 - comentario.nota)}
              <p>{comentario.comentario}</p>
              <button
                className="perfil-btn-editar-comentario"
                onClick={() => handleEditarAvaliacao(comentario)}
              >
                Editar
              </button>
              <button
                className="perfil-btn-excluir-comentario"
                onClick={() => handleExcluirAvaliacao(comentario.avaliacaoId)}
              >
                Excluir
              </button>
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
              onChange={(e) => setUserInfo({ ...userInfo, nome: e.target.value })}
            />

            <label htmlFor="email">E-mail:</label>
            <input
              type="email"
              id="email"
              value={userInfo.email}
              onChange={(e) => setUserInfo({ ...userInfo, email: e.target.value })}
            />

            <label htmlFor="senha">Senha (obrigatória para salvar):</label>
            <input
              type="password"
              id="senha"
              placeholder="Digite sua senha"
              value={userInfo.senha}
              onChange={(e) => setUserInfo({ ...userInfo, senha: e.target.value })}
            />
          </form>
        </section>

        <div className="perfil-acoes-topo">
          <button type="submit" className="perfil-btn-salvar">
            Salvar Alterações
          </button>

          <button type="button" className="perfil-btn-sair" onClick={handleLogout}>
            <LogOut size={20} /> Sair da Conta
          </button>
        </div>
        <div className="perfil-botoes-finais">
          <button type="button" className="perfil-btn-desativar" onClick={handleDesativarConta}>
            Desativar Conta
          </button>
          <button type="button" className="perfil-btn-excluir" onClick={handleExcluirConta}>
            Excluir Conta
          </button>
        </div>
      </main>
    </>
  );
}

export default Perfil;
