document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formCadastro");

  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const nome = document.getElementById("nomeEntrada").value.trim();
    const matricula = document.getElementById("matriculaEntrada").value.trim();
    const email = document.getElementById("emailEntrada").value.trim();
    const senha = document.getElementById("entradaSenha").value.trim();
    const dataNascimento = document.getElementById("nascimentoEntrada").value;

    // Validação básica
    if (matricula.length !== 13 || !/^\d{13}$/.test(matricula)) {
      alert("A matrícula deve conter exatamente 13 dígitos numéricos.");
      return;
    }

    if (senha.length < 6 || senha.length > 8) {
      alert("A senha deve ter entre 6 e 8 caracteres.");
      return;
    }

    const aluno = {
      nome,
      matricula,
      email,
      senha,
      dataNascimento,
      status: true // Ativo por padrão
    };

    console.log("Enviando:", aluno);

    try {
      const response = await fetch("http://localhost:8081/alunos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(aluno)
      });

      if (response.status === 201) {
        alert("Cadastro realizado com sucesso!");
        window.location.href = "entrar.html";
      } else if (response.status === 409) {
        const mensagem = await response.text();
        alert("Erro: " + mensagem);
      } else {
        alert("Erro ao cadastrar. Tente novamente.");
      }

    } catch (error) {
      console.error("Erro de rede:", error);
      alert("Erro de conexão com o servidor.");
    }
  });
});
