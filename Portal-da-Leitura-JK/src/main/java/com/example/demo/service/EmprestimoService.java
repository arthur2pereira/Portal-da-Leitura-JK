    package com.example.demo.service;

    import com.example.demo.dto.EmprestimoDTO;
    import com.example.demo.model.*;
    import com.example.demo.repository.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class EmprestimoService {

        @Autowired
        private EmprestimoRepository emprestimoRepository;

        @Autowired
        private BibliotecarioRepository bibliotecarioRepository;

        @Autowired
        private AlunoRepository alunoRepository;

        @Autowired
        private LivroRepository livroRepository;

        public List<EmprestimoModel> buscarPorAluno(String matricula) {
            return emprestimoRepository.findByAlunoMatricula(matricula);
        }

        public List<EmprestimoModel> buscarPorBibliotecario(Long bibliotecarioId) {
            return emprestimoRepository.findByBibliotecarioBibliotecarioId(bibliotecarioId);
        }

        public List<EmprestimoModel> listarTodos() {
            return emprestimoRepository.findAll();
        }

        public boolean estaVencido(Long emprestimoId) {
            EmprestimoModel emprestimo = emprestimoRepository.findByEmprestimoId(emprestimoId).orElse(null);
            return emprestimo != null && emprestimo.getDataDevolucao() == null && emprestimo.getDataVencimento()
                                                    .isBefore(LocalDate.now());
        }

        public EmprestimoModel registrarEmprestimo(EmprestimoDTO dto) {
            AlunoModel aluno = alunoRepository.findByMatricula(dto.getMatricula()).orElseThrow();
            LivroModel livro = livroRepository.findByLivroId(dto.getLivroId()).orElseThrow();
            BibliotecarioModel bibliotecario = bibliotecarioRepository.findByBibliotecarioId(dto.getBibliotecarioId()).orElseThrow();

            if (livro.getQuantidade() <= 0) throw new RuntimeException("Livro indisponível.");

            EmprestimoModel emprestimo = new EmprestimoModel();
            emprestimo.setAluno(aluno);
            emprestimo.setLivro(livro);
            emprestimo.setBibliotecario(bibliotecario);
            emprestimo.setDataEmprestimo(LocalDate.now());
            emprestimo.setDataVencimento(LocalDate.now().plusDays(7));

            return emprestimoRepository.save(emprestimo);
        }

        public void registrarDevolucao(Long emprestimoId) {
            EmprestimoModel emprestimo = emprestimoRepository.findByEmprestimoId(emprestimoId)
                    .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado."));
            emprestimo.setDataDevolucao(LocalDate.now());
            emprestimoRepository.save(emprestimo);
        }

        public boolean renovarEmprestimoPorAluno(Long emprestimoId, String matriculaAluno) {
            EmprestimoModel emprestimo = emprestimoRepository.findByEmprestimoId(emprestimoId)
                    .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

            if (!emprestimo.getAluno().getMatricula().equals(matriculaAluno)) {
                throw new RuntimeException("Você não tem permissão pra renovar esse empréstimo");
            }
            if (emprestimo.getDataDevolucao() != null) {
                throw new RuntimeException("Esse empréstimo já foi finalizado");
            }
            if (emprestimo.getRenovacoes() >= 1) {
                throw new RuntimeException("Você só pode renovar uma vez");
            }
            emprestimo.setDataVencimento(emprestimo.getDataVencimento().plusDays(7));
            emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);

            emprestimoRepository.save(emprestimo);
            return true;
        }

        public void renovarEmprestimoPorBibliotecario(Long emprestimoId, int dias, Long bibliotecarioId) {
            if (!bibliotecarioRepository.existsById(bibliotecarioId)) {
                throw new RuntimeException("Ação não permitida: bibliotecário não encontrado ou não autorizado.");
            }
            EmprestimoModel emprestimo = emprestimoRepository.findByEmprestimoId(emprestimoId)
                    .orElseThrow(() -> new RuntimeException("Empréstimo com ID " + emprestimoId + " não foi encontrado."));
            if (emprestimo.getDataDevolucao() != null) {
                throw new RuntimeException("Esse empréstimo já foi finalizado em " + emprestimo.getDataDevolucao() + ".");
            }
            if (dias < 7 || dias > 20) {
                throw new RuntimeException("Número de dias inválido. O valor deve estar entre 7 e 20 dias.");
            }
            emprestimo.setDataVencimento(emprestimo.getDataVencimento().plusDays(dias));
            emprestimo.setRenovacoes(emprestimo.getRenovacoes() + 1);
            emprestimoRepository.save(emprestimo);
        }

        public int diasDeAtraso(Long emprestimoId) {
            EmprestimoModel emprestimo = emprestimoRepository.findByEmprestimoId(emprestimoId).orElseThrow();
            if (emprestimo.getDataDevolucao() == null) return 0;
            return (int) ChronoUnit.DAYS.between(emprestimo.getDataVencimento(), emprestimo.getDataDevolucao());
        }

        public EmprestimoDTO converterParaDTO(EmprestimoModel emprestimo) {
            return new EmprestimoDTO(
                    emprestimo.getEmprestimoId(),
                    emprestimo.getAluno().getMatricula(),
                    emprestimo.getLivro().getTitulo(),
                    emprestimo.getLivro().getLivroId(),
                    emprestimo.getBibliotecario().getBibliotecarioId(),
                    emprestimo.getDataEmprestimo(),
                    emprestimo.getDataVencimento(),
                    emprestimo.getDataDevolucao(),
                    emprestimo.getRenovacoes(),
                    emprestimo.getStatus()
            );
        }

        public List<EmprestimoDTO> converterParaDTO(List<EmprestimoModel> emprestimos) {
            return emprestimos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
        }
    }

