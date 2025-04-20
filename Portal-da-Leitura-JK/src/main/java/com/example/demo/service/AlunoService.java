package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.EmprestimoDTO;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.AlunoModel;
import com.example.demo.model.EmprestimoModel;
import com.example.demo.model.ReservaModel;
import com.example.demo.repository.EmprestimoRepository;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    public List<EmprestimoDTO> listarEmprestimos(String matricula) {
        List<EmprestimoModel> emprestimos = emprestimoRepository.findByAlunoMatricula(matricula);
        return emprestimos.stream().map(this::converterParaDTO).toList();
    }

    // Busca uma reserva ativa de um aluno
    public Optional<ReservaDTO> buscarReservaAtiva(String matricula) {
        Optional<ReservaModel> reserva = reservaRepository.findReservaAtivaByAlunoMatricula(matricula);
        return reserva.map(this::converterParaDTO);
    }

    public Optional<AlunoModel> buscarPorMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula);
    }

    public List<AlunoModel> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public boolean alunoExiste(String matricula) {
        return alunoRepository.existsByMatricula(matricula);
    }


    public AlunoModel salvarDTO(AlunoDTO dto) {
        AlunoModel aluno = new AlunoModel(
                dto.getMatricula(),
                dto.getNome(),
                dto.getEmail(),
                dto.getSenha(),
                dto.getStatus().equals("Ativo")
        );
        return alunoRepository.save(aluno);
    }


    public Optional<AlunoModel> autenticar(String matricula, String senha) {
        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);

        System.out.println("Matrícula buscada: " + matricula);

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            System.out.println("Senha armazenada: " + aluno.getSenha());
            if (aluno.getSenha().equals(senha)) {
                return Optional.of(aluno);
            }
        }

        return Optional.empty();
    }

    public AlunoDTO converterParaDTO(AlunoModel aluno) {
        return new AlunoDTO(
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getSenha(),
                aluno.getStatus()
        );
    }

    private EmprestimoDTO converterParaDTO(EmprestimoModel model) {
        return new EmprestimoDTO(
                model.getEmprestimoId(),
                model.getDataEmprestimo(),
                model.getDataVencimento(),
                model.getDataDevolucao(),
                model.getLivro().getTitulo(),
                model.getAluno().getMatricula()
        );
    }

    private ReservaDTO converterParaDTO(ReservaModel model) {
        return new ReservaDTO(
                model.getReservaId(),
                model.getDataReserva(),
                model.getDataVencimento(),
                model.getLivro().getTitulo(),
                model.getAluno().getMatricula()
        );
    }
}
