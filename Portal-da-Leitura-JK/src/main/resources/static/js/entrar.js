document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
  
    form.addEventListener('submit', async (event) => {
      event.preventDefault();
  
      const matricula = document.getElementById('matriculaEstatica').value.trim();
      const senha = document.getElementById('entradaSenha').value.trim();
  
      if (matricula.length !== 13) {
        alert('A matrícula deve ter exatamente 13 caracteres.');
        return;
      }
  
      if (senha.length < 6 || senha.length > 8) {
        alert('A senha deve ter entre 6 e 8 caracteres.');
        return;
      }
  
      try {
        const response = await fetch('http://localhost:8081/alunos/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ matricula, senha })
        });
  
        if (response.ok) {
          alert('Login realizado com sucesso!');
          window.location.href = 'menuprincipal.html';
        } else {
          alert('Matrícula ou senha inválida.');
        }
      } catch (err) {
        alert(`Erro de conexão: ${err}`);
      }
    });
  });
  
  