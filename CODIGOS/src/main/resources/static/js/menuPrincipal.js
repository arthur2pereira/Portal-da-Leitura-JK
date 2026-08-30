document.addEventListener('DOMContentLoaded', () => {
    const nomeAluno = localStorage.getItem('nomeAluno');
    const saudacao = document.getElementById('saudacaoUsuario');
    const botaoAluno = document.getElementById('botaoAluno');
    const linkAluno = document.getElementById('linkAluno');
  
    if (nomeAluno) {
      saudacao.textContent = `Bem-vindo(a), ${nomeAluno}!`;
      botaoAluno.style.display = 'block';
      linkAluno.style.display = 'inline-block';
    } else {
      saudacao.textContent = '';
      botaoAluno.style.display = 'none';
      linkAluno.style.display = 'none';
    }
  });
  