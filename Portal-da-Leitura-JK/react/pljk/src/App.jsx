import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

// Componentes
import Navbar from './componentes/navbar'
import Footer from './componentes/footer'
import Livros from './componentes/livros'

// Paginas
import Home from './pag/index' //pagina inicial
import Livro from './pag/livro' // tela do livro
import Catalogo from './pag/catalogo' // tela do catalogo dos livros
import Cadastro from './pag/cadastro'// tela do cadastro
import Login from './pag/login' // tela do login

// Aluno
import NotificacoesAluno from './pag/alunos/notificacoes' // tela de notificações 
import Perfil from './pag/alunos/perfil' // tela de perfil do aluno

// Bibliotecário
import AdminAlunos from './pag/bibliotecarios/admin-alunos' // tela de gerenciamento de alunos do adm
import AdminLivros from './pag/bibliotecarios/admin-livros' // tela de gerenciamento de livros do adm
import AdminNotificacoes from './pag/bibliotecarios/admin-notificacoes'// tela de gerenciamento de notificações do adm
import AdminReservas from './pag/bibliotecarios/admin-reservas' // tela de gerenciamento de reservas do adm

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/livro" element={<Livro />} />
        <Route path="/Catalogo" element={<Catalogo />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />

        <Route path="/aluno/perfil" element={<Perfil />} />
        <Route path="/aluno/notificacoes" element={<NotificacoesAluno />} />

        <Route path="/admin/livros" element={<AdminLivros />} />
        <Route path="/admin/alunos" element={<AdminAlunos />} />
        <Route path="/admin/reservas" element={<AdminReservas />} />
        <Route path="/admin/notificacoes" element={<AdminNotificacoes />} />
      </Routes>
      <Footer />
    </Router>
  )
}

export default App
