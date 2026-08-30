package com.example.demo.service;

import com.example.demo.dto.ContasInativasDTO;
import com.example.demo.model.AlunoModel;
import com.example.demo.model.ContasInativasModel;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ContasInativasRepository;
import com.example.demo.repository.EmprestimoRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContasInativasService {

    @Autowired
    private ContasInativasRepository contasInativasRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ContasInativasService(ContasInativasRepository contasInativasRepository,
                                 EmprestimoRepository emprestimoRepository,
                                 ReservaRepository reservaRepository,
                                 AlunoRepository alunoRepository) {
        this.contasInativasRepository = contasInativasRepository;
        this.alunoRepository = alunoRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.reservaRepository = reservaRepository;
    }

    private void validarSemPendencias(String matricula) {
        boolean temEmprestimos = !emprestimoRepository.findByAlunoMatricula(matricula).isEmpty();
        boolean temReservas = !reservaRepository.findReservasByAlunoMatricula(matricula).isEmpty();

        if (temEmprestimos || temReservas) {
            throw new IllegalStateException("Não é possível desativar/excluir. O aluno possui pendências ativas.");
        }
    }

    public ContasInativasDTO desativarConta(String matricula, String senha) {
        AlunoModel aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        if (!passwordEncoder.matches(senha, aluno.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        validarSemPendencias(matricula);

        aluno.setStatus(false);
        alunoRepository.save(aluno);

        ContasInativasModel conta = new ContasInativasModel();
        conta.setAluno(aluno);
        conta.setNome(aluno.getNome());
        conta.setEmail(aluno.getEmail());
        conta.setMotivo(ContasInativasModel.Motivo.DESATIVACAO);
        conta.setDataSolicitacao(LocalDateTime.now());

        return new ContasInativasDTO(contasInativasRepository.save(conta));
    }

    public ContasInativasDTO solicitarExclusao(String matricula, String senha) {
        AlunoModel aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        if (!passwordEncoder.matches(senha, aluno.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        validarSemPendencias(matricula);

        aluno.setStatus(false);
        alunoRepository.save(aluno);

        ContasInativasModel conta = new ContasInativasModel();
        conta.setAluno(aluno);
        conta.setNome(aluno.getNome());
        conta.setEmail(aluno.getEmail());
        conta.setMotivo(ContasInativasModel.Motivo.EXCLUSAO);
        conta.setDataSolicitacao(LocalDateTime.now());
        conta.setDataExclusaoFinal(LocalDateTime.now().plusDays(30));

        return new ContasInativasDTO(contasInativasRepository.save(conta));
    }

    public void reativarConta(String matricula) {
        ContasInativasModel conta = contasInativasRepository.findByAluno_Matricula(matricula)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada nas inativas."));
        AlunoModel aluno = conta.getAluno();
        aluno.setStatus(true);
        alunoRepository.save(aluno);
        contasInativasRepository.delete(conta);
    }


    public void processarExclusoesDefinitivas() {
        List<ContasInativasModel> contasParaExcluir =
                contasInativasRepository.findByDataExclusaoFinalBefore(LocalDateTime.now());

        for (ContasInativasModel conta : contasParaExcluir) {
            alunoRepository.deleteByMatricula(conta.getAluno().getMatricula());
            contasInativasRepository.delete(conta);
        }
    }

    public List<ContasInativasDTO> listarTodas() {
        return contasInativasRepository.findAll()
                .stream()
                .map(ContasInativasDTO::new)
                .collect(Collectors.toList());
    }
}
