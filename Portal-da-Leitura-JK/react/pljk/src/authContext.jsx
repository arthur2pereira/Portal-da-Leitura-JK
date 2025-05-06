import { createContext, useContext, useState, useEffect } from "react"

const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(null)

  useEffect(() => {
    const authLocal = localStorage.getItem("auth")
    if (authLocal) {
      setAuth(JSON.parse(authLocal))
    }
  }, [])

  const login = (dados) => {
    console.log("Token recebido e salvo:", dados.token); 
    localStorage.setItem("auth", JSON.stringify(dados))
    setAuth(dados)
  }

  const logout = () => {
    localStorage.removeItem("auth")
    setAuth(null)
  }

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
