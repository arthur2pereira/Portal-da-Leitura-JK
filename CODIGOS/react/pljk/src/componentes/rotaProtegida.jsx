import { Navigate } from "react-router-dom"

const RotaProtegida = ({ children, tipoPermitido }) => {
  const auth = JSON.parse(localStorage.getItem("auth"))

  if (!auth || auth.tipo !== tipoPermitido) {
    return <Navigate to="/" replace />
  }

  return children
}

export default RotaProtegida
