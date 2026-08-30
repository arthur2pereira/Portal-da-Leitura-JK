document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("filtro-form");
    const listaLivros = document.getElementById("lista-livros");
  
    buscarLivros();
  
    form.addEventListener("submit", (e) => {
      e.preventDefault();
  
      const filtros = {
        titulo: document.getElementById("titulo").value,
        autor: document.getElementById("autor").value,
        genero: document.getElementById("genero").value,
        curso: document.getElementById("curso").value,
      };
  
      buscarLivros(filtros);
    });
  
    async function buscarLivros(filtros = {}) {
      try {
        const params = new URLSearchParams(filtros).toString();
        const response = await fetch(`/livros/disponiveis?${params}`);
  
        if (!response.ok) {
          throw new Error("Erro ao buscar livros");
        }
  
        const livros = await response.json();
        mostrarLivros(livros);
      } catch (error) {
        console.error(error);
        listaLivros.innerHTML = `<p class="text-danger">Erro ao carregar livros.</p>`;
      }
    }
  
    function mostrarLivros(livros) {
      listaLivros.innerHTML = "";
  
      if (livros.length === 0) {
        listaLivros.innerHTML = `<p class="text-muted">Nenhum livro encontrado.</p>`;
        return;
      }
  
      livros.forEach((livro) => {
        const col = document.createElement("div");
        col.classList.add("col-md-6");
  
        col.innerHTML = `
          <div class="card h-100">
            <div class="card-body">
              <h5 class="card-title">${livro.titulo}</h5>
              <p class="card-text"><strong>Autor:</strong> ${livro.autor}</p>
              <p class="card-text"><strong>Gênero:</strong> ${livro.genero}</p>
              <p class="card-text"><strong>Curso:</strong> ${livro.curso}</p>
              <p class="card-text"><strong>Ano:</strong> ${livro.anoPublicacao}</p>
              <p class="card-text"><strong>Disponível:</strong> ${livro.quantidade}</p>
              <p class="card-text small text-muted">${livro.descricao}</p>
            </div>
          </div>
        `;
  
        listaLivros.appendChild(col);
      });
    }
  });
  