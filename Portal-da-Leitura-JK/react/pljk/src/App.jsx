import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom'
import { AuthProvider } from "./authContext.jsx";

// Componentes
import Navbar from './componentes/navbar'
import Footer from './componentes/footer'
import Scroll from './componentes/scroll.jsx'

// Paginas
import Home from './pag/home'
import Livro from './pag/livro'
import Catalogo from './pag/catalogo'
import Cadastro from './pag/cadastro'
import Login from './pag/login'

// Aluno
import NotificacoesAluno from './pag/alunos/notificacoes'
import Perfil from './pag/alunos/perfil'

// Bibliotecário
import AdminAlunos from './pag/bibliotecarios/admin-alunos'
import AdminLivros from './pag/bibliotecarios/admin-livros'
import AdminNotificacoes from './pag/bibliotecarios/admin-notificacoes'
import AdminReservas from './pag/bibliotecarios/admin-reservas'
import AdminArea from './pag/bibliotecarios/admin-area'
import AdminEmprestimos from './pag/bibliotecarios/admin-emprestimo.jsx'
import AdminPenalidades from './pag/bibliotecarios/admin-penalidades.jsx'

import RotaProtegida from "./componentes/RotaProtegida"

function AppRoutes() {
  const location = useLocation()

  const rotasSemLayout = ['/login', '/cadastro']
  const esconderLayout = rotasSemLayout.includes(location.pathname)

  return (
    <>
      <Navbar />
      <Scroll /> 
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
          <Route path="/admin/emprestimos" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminEmprestimos />
            </RotaProtegida>
          } />
          <Route path="/admin/notificacoes" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminNotificacoes />
            </RotaProtegida>
          } />
          <Route path="/admin/penalidades" element={
            <RotaProtegida tipoPermitido="bibliotecario">
              <AdminPenalidades />
            </RotaProtegida>
          }/>
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
