package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PenalidadeRepository penalidadeRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AlunoService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<EmprestimoDTO> listarEmprestimos(String matricula) {
        List<EmprestimoModel> emprestimos = emprestimoRepository.findByAlunoMatricula(matricula);
        if (emprestimos.isEmpty()) {
            return List.of();
        }
        return emprestimos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    public List<NotificacaoDTO> listarNotificacoes(String matricula) {
        List<NotificacaoModel> notificacoes = notificacaoRepository.findByAlunoMatricula(matricula);
        if (notificacoes.isEmpty()) {
            return List.of();
        }
        return notificacoes.stream()
                .map(n -> new NotificacaoDTO(
                        n.getNotificacaoId(),
                        n.getAluno().getMatricula(),
                        n.getMensagem(),
                        n.getTipo(),
                        n.isLida()
                ))
                .collect(Collectors.toList());
    }

    public List<AvaliacaoDTO> listarAvaliacoes(String matricula) {
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByAlunoMatricula(matricula);
        if (avaliacoes.isEmpty()) {
            return List.of();
        }
        return avaliacoes.stream()
                .map(a -> new AvaliacaoDTO(
                        a.getAvaliacaoId(),
                        a.getAluno().getMatricula(),
                        a.getLivro().getLivroId(),
                        a.getNota(),
                        a.getComentario()
                ))
                .collect(Collectors.toList());
    }

    public List<PenalidadeDTO> listarPenalidades(String matricula) {
        List<PenalidadeModel> penalidades = penalidadeRepository.findByAlunoMatricula(matricula);
        if (penalidades.isEmpty()) {
            return List.of();
        }
        return penalidades.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

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
        if (alunoRepository.existsByMatricula(dto.getMatricula())) {
            throw new IllegalArgumentException("Matrícula já cadastrada.");
        }

        if (dto.getSenha().length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }

        String senhaCodificada = passwordEncoder.encode(dto.getSenha());

        AlunoModel aluno = new AlunoModel(
                dto.getMatricula(),
                dto.getNome(),
                dto.getEmail(),
                senhaCodificada,
                true
        );
        return alunoRepository.save(aluno);
    }

    public void deletarAlunoPorMatricula(String matricula) {
        alunoRepository.deleteByMatricula(matricula);
    }

    public Optional<AlunoModel> autenticar(String matricula, String email, String senha) {
        Optional<AlunoModel> alunoOpt;

        if (matricula != null && !matricula.isEmpty()) {
            alunoOpt = alunoRepository.findByMatricula(matricula);
            System.out.println("Matrícula buscada: " + matricula);
        } else if (email != null && !email.isEmpty()) {
            alunoOpt = alunoRepository.findByEmail(email);
            System.out.println("Email buscado: " + email);
        } else {
            return Optional.empty();
        }

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            System.out.println("Senha armazenada: " + aluno.getSenha());
            if (!passwordEncoder.matches(senha, aluno.getSenha())) {
                return Optional.empty();
            }
            return Optional.of(aluno);
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
                model.getAluno().getMatricula(),
                model.getLivro().getLivroId(),
                model.getBibliotecario().getBibliotecarioId(),
                model.getDataEmprestimo(),
                model.getDataVencimento(),
                model.getDataDevolucao(),
                model.getRenovacoes(),
                model.getStatus()
        );
    }

    private ReservaDTO converterParaDTO(ReservaModel model) {
        return new ReservaDTO(
                model.getReservaId(),
                model.getAluno().getMatricula(),
                model.getLivro().getLivroId(),
                model.getStatus(),
                model.getDataReserva(),
                model.getDataVencimento()
        );
    }

    private PenalidadeDTO converterParaDTO(PenalidadeModel model) {
        return new PenalidadeDTO(
                model.getPenalidadeId(),
                model.getAluno().getMatricula(),
                model.getMotivo(),
                model.getTipo(),
                model.getDataAplicacao(),
                model.getDiasBloqueio()
        );
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AlunoModel aluno = alunoRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aluno não encontrado"));

        return User.builder()
                .username(aluno.getEmail())
                .password(aluno.getSenha())
                .roles("USER")
                .build();
    }
}
