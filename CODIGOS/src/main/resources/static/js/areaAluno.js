
// Marcar todas as notificações como lidas
document.addEventListener("DOMContentLoaded", () => {
    const btnLerTudo = document.getElementById("marcarTodasLidas");
  
    if (btnLerTudo) {
      btnLerTudo.addEventListener("click", () => {
        const notificacoes = document.querySelectorAll(".notificacao.nao-lida");
        notificacoes.forEach(n => {
          n.classList.remove("nao-lida");
          n.style.opacity = "0.6";
        });
      });
    }
  
    // Previne envio do form (só pra demonstração)
    const formConfig = document.querySelector("#configuracoes form");
    if (formConfig) {
      formConfig.addEventListener("submit", (e) => {
        e.preventDefault();
        alert("Configurações salvas com sucesso! (Simulação)");
      });
    }
  });
  