import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom'
import { AuthProvider } from "./authContext.jsx";

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
import AdminArea from './pag/bibliotecarios/admin-area'

import RotaProtegida from "./componentes/RotaProtegida"

function AppRoutes() {
  const location = useLocation()

  const rotasSemLayout = ['/login', '/cadastro']
  const esconderLayout = rotasSemLayout.includes(location.pathname)

  return (
    <>
      <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/livro/:livroId" element={<Livro />} />
          <Route path="/Catalogo" element={<Catalogo />} />
          <Route path="/login" element={<Login />} />
          <Route path="/cadastro" element={<Cadastro />} />
          <Route path="/aluno/perfil" element={
            <RotaProtegida tipoPermitido="aluno">
              <Perfil />
            </RotaProtegida>
          } />
          <Route path="/aluno/notificacoes" element={
            <RotaProtegida tipoPermitido="aluno">
              <NotificacoesAluno />
            </RotaProtegida>
          } />
          <Route path="/admin/area" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminArea />
            </RotaProtegida>
          } />
          <Route path="/admin/livros" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminLivros />
            </RotaProtegida>
          } />
          <Route path="/admin/alunos" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminAlunos />
            </RotaProtegida>
          } />
          <Route path="/admin/reservas" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminReservas />
            </RotaProtegida>
          } />
          <Route path="/admin/notificacoes" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminNotificacoes />
            </RotaProtegida>
          } />
        </Routes>
      {!esconderLayout && <Footer />}
    </>
  )
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  )
}



export default App
