import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(null);

  useEffect(() => {
    const storedAuth = localStorage.getItem("auth");
    if (storedAuth) {
      setAuth(JSON.parse(storedAuth));
    }
  }, []);

  const login = async (dados) => {
    try {
      if (dados.tipo === "aluno") {
        const payloadBase64 = dados.token.split('.')[1];
        const payload = JSON.parse(atob(payloadBase64));
        const matricula = payload.sub;

        const res = await fetch(`http://localhost:8081/alunos/${matricula}`, {
          headers: {
            Authorization: `Bearer ${dados.token}`,
          },
        });

        if (!res.ok) throw new Error("Falha ao buscar dados do aluno");

        const alunoData = await res.json();

        const authData = {
          ...dados,
          matricula,
          nome: alunoData.nome,
          email: alunoData.email,
        };

        localStorage.setItem("auth", JSON.stringify(authData));
        setAuth(authData);

      } else if (dados.tipo === "bibliotecario") {
        const email = dados.email;

        if (!email) throw new Error("Email do bibliotecário não encontrado");

        const res = await fetch(`http://localhost:8081/bibliotecarios/${encodeURIComponent(email)}`, {
          headers: { Authorization: `Bearer ${dados.token}` }
        });

        if (!res.ok) throw new Error("Falha ao buscar dados do bibliotecário");

        const bibData = await res.json();

        const authData = {
          ...dados,
          nome: bibData.nome,
          email: bibData.email,
        };

        localStorage.setItem("auth", JSON.stringify(authData));
        setAuth(authData);
      }
    } catch (error) {
      console.error("Erro no login:", error);
    }
  };

  const logout = () => {
    localStorage.removeItem("auth");
    setAuth(null);
  };

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
