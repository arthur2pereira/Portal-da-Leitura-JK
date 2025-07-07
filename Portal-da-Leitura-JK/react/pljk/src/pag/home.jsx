import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import '../assets/css/home.css';

import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay, Navigation, Pagination, EffectFade } from 'swiper/modules';

import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/effect-fade';

const Estrelas = ({ nota }) => {
  const estrelasCheias = Math.floor(nota);
  const meiaEstrela = nota % 1 >= 0.5;
  const estrelasVazias = 5 - estrelasCheias - (meiaEstrela ? 1 : 0);

  return (
    <div className="estrelas">
      {'★'.repeat(estrelasCheias)}
      {meiaEstrela && '⯪'}
      {'☆'.repeat(estrelasVazias)}
    </div>
  );
};

function BlocoLivros() {
  const [livrosDestaque, setLivrosDestaque] = useState([]);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    setCarregando(true);

    fetch("http://localhost:8081/avaliacoes/mais-avaliados")
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao buscar livros");
        return res.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          const livrosProcessados = data.map(item => {
            const livro = item.livro;
            const notas = item.notas || [];

            const numeroAvaliacoes = notas.length;
            const notaMedia = numeroAvaliacoes > 0
              ? notas.reduce((acc, n) => acc + n, 0) / numeroAvaliacoes
              : 0;

            return {
              ...livro,
              notaMedia,
              numeroAvaliacoes
            };
          });

          setLivrosDestaque(livrosProcessados);
        } else {
          setLivrosDestaque([]);
        }
      })
      .catch((err) => {
        console.error("Erro ao buscar livros mais avaliados:", err);
        setLivrosDestaque([]);
      })
      .finally(() => {
        setCarregando(false);
      });
  }, []);

  return (
    <section className="bloco-livros">
      <h3>Livros em Destaque</h3>
      {carregando ? (
        <p>Carregando livros em destaque...</p>
      ) : livrosDestaque.length === 0 ? (
        <p className="mensagem-vazia">Nenhum livro avaliado ainda.</p>
      ) : (
        <div className="linha-livros">
          {livrosDestaque.map((livro) => (
            <Link to={`/livros/${livro.livroId || livro.id}`} key={livro.livroId || livro.id} className="livro-card">
              <h4>{livro.titulo}</h4>
              <Estrelas nota={livro.notaMedia} />
              <p className="avaliacoes-info">{livro.numeroAvaliacoes} avaliação{livro.numeroAvaliacoes !== 1 ? 's' : ''}</p>
              <p>{livro.descricao}</p>
            </Link>
          ))}
        </div>
      )}
    </section>
  );
}

const BlocoImagem = ({ dados, titulo, tipo }) => (
  <section className="bloco-imagem">
    <h3>{titulo}</h3>
    <div className="linha-imagens">
      {dados.map((item, index) => (
        <Link key={index} to={`/livros?${tipo}=${item.nome}`} className="item-link">
          <img src={item.imagem} alt={item.nome} className="bolinha" />
          <p>{item.nome}</p>
        </Link>
      ))}
    </div>
  </section>
);

const Destaques = () => (
  <div className="container-destaques">
    <BlocoLivros />
    <BlocoImagem dados={autoresBrasileiros} titulo="Autores Brasileiros" tipo="autor" />
    <BlocoImagem dados={autoresEstrangeiros} titulo="Autores Estrangeiros" tipo="autor" />
    <BlocoImagem dados={editoras} titulo="Editoras" tipo="editora" />
  </div>
);

const autoresBrasileiros = [
  { nome: 'Jorge Amado', imagem: '/imagens/autores/brasileiros/amado.jpg' },
  { nome: 'Clarice Lispector', imagem: '/imagens/autores/brasileiros/clarice.jpg' },
  { nome: 'Carlos D. de Andrade', imagem: '/imagens/autores/brasileiros/Drummond.jpg' },
  { nome: 'José de Alencar', imagem: '/imagens/autores/brasileiros/josé.png' },
  { nome: 'Machado de Assis', imagem: '/imagens/autores/brasileiros/Machado.jpg' },
  { nome: 'Mário de Andrade', imagem: '/imagens/autores/brasileiros/mario.png' }
];

const autoresEstrangeiros = [
  { nome: 'George Orwell', imagem: '/imagens/autores/gringos/george orwell.jpg' },
  { nome: 'J.K. Rowling', imagem: '/imagens/autores/gringos/JK.jpg' },
  { nome: 'Kazuo Ishiguro', imagem: '/imagens/autores/gringos/kazuo ishiguro.jpg' },
  { nome: 'Willian Shakespaere', imagem: '/imagens/autores/gringos/shakespaere.jpg' },
  { nome: 'Victor Hugo', imagem: '/imagens/autores/gringos/victor hugo.jpg' },
  { nome: 'Stephen King', imagem: '/imagens/autores/gringos/sthepen king.jpg' }
];

const editoras = [
  { nome: 'Companhia das Letras', imagem: '/imagens/editora/CompLetras.jpg' },
  { nome: 'Arqueiro', imagem: '/imagens/editora/arqueiro.jpg' },
  { nome: 'Rocco', imagem: '/imagens/editora/rocco.jpg' },
  { nome: 'Editora do Brasil', imagem: '/imagens/editora/editoraDoBrasil.png' },
  { nome: 'Globo', imagem: '/imagens/editora/editoraGlobo.png' },
  { nome: 'Saraiva', imagem: '/imagens/editora/saraiva.jpg' }
];

const imagens = [
  "/imagens/biblioteca.png",
  "/imagens/Biblioteca2.jpg",
  "/imagens/Biblioteca3.jpg",
];

function Index() {
  return (
    <div className="index-wrapper">
      <section className="carrossel">
        <Swiper
          modules={[Autoplay, Navigation, Pagination, EffectFade]}
          slidesPerView={1}
          loop={true}
          autoplay={{ delay: 15000, disableOnInteraction: false }}
          pagination={{ clickable: true }}
          effect="fade"
          className="carrossel-container"
        >
          {imagens.map((src, i) => (
            <SwiperSlide key={i}>
              <img src={src} alt={`Slide ${i + 1}`} className="carrossel-img" />
            </SwiperSlide>
          ))}
        </Swiper>
        <div className="carrossel-overlay">
          <h1>Portal da Leitura Juscelino Kubitschek</h1>
        </div>
      </section>

      <Destaques />

      <section className="sobre-biblioteca" id="sobre">
        <div className="container">
          <h2>Sobre a Biblioteca</h2>
          <p>
            A Biblioteca Juscelino Kubitschek é um espaço dedicado ao incentivo à leitura, à pesquisa e ao aprendizado contínuo. Com um acervo diversificado que abrange literatura brasileira, estrangeira, obras técnicas dos cursos da escola e materiais de apoio escolar, ela se destaca como um ambiente acolhedor e silencioso, ideal para o desenvolvimento intelectual dos alunos.
            A missão da biblioteca é servir como um ponto de encontro entre conhecimento, cultura e comunidade, reforçando a importância da leitura no processo de formação pessoal e profissional dos alunos.
          </p>
          <p>
            Mais do que um local para empréstimos de livros, a biblioteca busca promover o hábito da leitura de forma leve e acessível, oferecendo atividades de incentivo como clubes de leitura, exposições temáticas e campanhas de doação. O espaço conta ainda com o apoio de bibliotecários capacitados, sempre prontos para orientar os estudantes na busca por informações e estimular o pensamento crítico.
          </p>
          <p>
            A missão da biblioteca é servir como um ponto de encontro entre conhecimento, cultura e comunidade, reforçando a importância da leitura no processo de formação pessoal e profissional dos alunos.
          </p>
        </div>
      </section>
    </div>
  );
}

export default Index;
