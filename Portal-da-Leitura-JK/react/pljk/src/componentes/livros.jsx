import React, { useEffect, useState } from "react";

function Livros() {
  const [livros, setLivros] = useState([]);

  useEffect(() => {
    // Aqui você faria uma requisição ao backend para pegar os livros
    setLivros([
      { id: 1, titulo: "Livro 1", autor: "Autor 1" },
      { id: 2, titulo: "Livro 2", autor: "Autor 2" },
      { id: 3, titulo: "Livro 3", autor: "Autor 3" },
    ]);
  }, []);

  return (
    <div className="livros">
      <h2>Catálogo de Livros</h2>
      <ul>
        {livros.map((livro) => (
          <li key={livro.id}>
            <h3>{livro.titulo}</h3>
            <p>{livro.autor}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Livros;
