package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {

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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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
                        n.getBibliotecario().getBibliotecarioId(),
                        n.getMensagem(),
                        n.getTipo(),
                        n.isLida()
                ))
                .collect(Collectors.toList());
    }

    public List<AlunoDTO> listarTodos() {
        List<AlunoModel> alunos = alunoRepository.findAll();
        return alunos.stream().map(aluno -> {
            AlunoDTO dto = new AlunoDTO();
            dto.setNome(aluno.getNome());
            dto.setMatricula(aluno.getMatricula());
            dto.setEmail(aluno.getEmail());
            return dto;
        }).collect(Collectors.toList());
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

    public List<ReservaDTO> listarReservas(String matricula) {
        List<ReservaModel> reservas = reservaRepository.findReservasByAlunoMatricula(matricula);
        return reservas.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservaDTO> buscarReservaAtiva(String matricula) {
        Optional<ReservaModel> reservaAtiva = reservaRepository.findReservaAtivaByAlunoMatricula(matricula);

        if (reservaAtiva.isPresent()) {
            return Optional.of(converterParaDTO(reservaAtiva.get()));
        } else {
            return Optional.empty();
        }
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

    @Transactional
    public void deletarAlunoPorMatricula(String matricula) {
        alunoRepository.deleteByMatricula(matricula);
    }

    public Optional<TokenDTO> autenticar(String matricula, String email, String senha) {
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

            String token = jwtUtil.generateToken(aluno.getMatricula(), "USER");

            AlunoDTO alunoDTO = new AlunoDTO();
            alunoDTO.setEmail(aluno.getEmail());
            alunoDTO.setNome(aluno.getNome());
            alunoDTO.setMatricula(aluno.getMatricula());

            return Optional.of(new TokenDTO(token, "USER", alunoDTO));
        }
        return Optional.empty();
    }

    public AlunoDTO atualizarAluno(AlunoDTO dto) {
        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(dto.getMatricula());

        if (alunoOpt.isEmpty()) {
            throw new IllegalArgumentException("Aluno não encontrado.");
        }

        AlunoModel aluno = alunoOpt.get();

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            aluno.setNome(dto.getNome());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            Optional<AlunoModel> alunoComMesmoEmail = alunoRepository.findByEmail(dto.getEmail());
            if (alunoComMesmoEmail.isPresent() && !alunoComMesmoEmail.get().getMatricula().equals(dto.getMatricula())) {
                throw new IllegalArgumentException("Esse email já está em uso por outro aluno.");
            }
            aluno.setEmail(dto.getEmail());
        }

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            if (dto.getSenha().length() < 6) {
                throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
            }
            aluno.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        alunoRepository.save(aluno);
        return converterParaDTO(aluno);
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
                model.getLivro().getTitulo(),
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
}
