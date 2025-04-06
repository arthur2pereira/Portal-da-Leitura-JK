
// Função para destacar o link da página atual no menu
document.addEventListener("DOMContentLoaded", () => {
    const links = document.querySelectorAll("header nav .nav-link");
    const urlAtual = window.location.pathname;
  
    links.forEach(link => {
      const href = link.getAttribute("href");
      if (urlAtual.includes(href)) {
        link.classList.add("fw-bold");
        link.style.color = "#1a936f";
      }
    });
  });
  