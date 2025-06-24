    package com.example.demo.service;

    import com.example.demo.dto.*;
    import com.example.demo.model.*;
    import com.example.demo.repository.*;
    import com.example.demo.security.CustomUserDetailsService;
    import com.example.demo.security.JwtUtil;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class BibliotecarioService {

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private LivroRepository livroRepository;

        @Autowired
        private BibliotecarioRepository bibliotecarioRepository;

        @Autowired
        private PenalidadeRepository penalidadeRepository;

        @Autowired
        private ReservaRepository reservaRepository;

        @Autowired
        private EmprestimoRepository emprestimoRepository;

        @Autowired
        private AvaliacaoRepository avaliacaoRepository;

        public LivroDTO salvarLivro(LivroDTO livroDTO) {
            LivroModel livro = new LivroModel();
            livro.setTitulo(livroDTO.getTitulo());
            livro.setAutor(livroDTO.getAutor());
            livro.setGenero(livroDTO.getGenero());
            livro.setCurso(livroDTO.getCurso());
            livro.setEditora(livroDTO.getEditora());
            livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
            livro.setDescricao(livroDTO.getDescricao());
            livro.setQuantidade(livroDTO.getQuantidade());

            LivroModel livroSalvo = livroRepository.save(livro);
            return new LivroDTO(livroSalvo);
        }


        public LivroDTO editarLivro (Long livroId, LivroDTO livroDTO) {
            LivroModel livro = livroRepository.findByLivroId(livroId)
                    .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

            livro.setTitulo(livroDTO.getTitulo());
            livro.setAutor(livroDTO.getAutor());
            livro.setGenero(livroDTO.getGenero());
            livro.setCurso(livroDTO.getCurso());
            livro.setEditora(livroDTO.getEditora());
            livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
            livro.setDescricao(livroDTO.getDescricao());
            livro.setQuantidade(livroDTO.getQuantidade());

            return new LivroDTO(livroRepository.save(livro));
        }

        public Optional<LivroDTO> buscarLivroPorId(Long livroId) {
            return livroRepository.findByLivroId(livroId)
                    .map(LivroDTO::new);
        }

        public boolean removerLivro(Long livroId) {
            return livroRepository.findByLivroId(livroId)
                    .map(livro -> {
                        livroRepository.delete(livro);
                        return true;
                    }).orElse(false);
        }

        public List<ReservaDTO> listarTodasReservas() {
            return reservaRepository.findAll().stream()
                    .map(ReservaDTO::new)
                    .toList();
        }

        public List<PenalidadeDTO> listarPenalidadesDoAluno(String matricula) {
            return penalidadeRepository.findByAlunoMatricula(matricula).stream()
                    .map(PenalidadeDTO::new)
                    .toList();
        }

        public List<EmprestimoDTO> listarEmprestimosDoAluno(String matricula) {
            return emprestimoRepository.findByAlunoMatricula(matricula).stream()
                    .map(EmprestimoDTO::new)
                    .toList();
        }

        public boolean removerComentario(Long avaliacaoId) {
            return avaliacaoRepository.findByAvaliacaoId(avaliacaoId)
                    .map(avaliacao -> {
                        avaliacaoRepository.delete(avaliacao);
                        return true;
                    }).orElse(false);
        }

        public Optional<TokenDTO> autenticar(String email, String senha) {
            List<String> emailsPermitidos = List.of(
                    "bibliotecarioetejk@gmail.com",
                    "bibliotecarioetejk2@gmail.com"
            );

            if (!emailsPermitidos.contains(email)) return Optional.empty();

            return bibliotecarioRepository.findByEmail(email)
                    .filter(b -> b.getSenha().equals(senha))
                    .map(b -> {
                        String token = jwtUtil.generateToken(String.valueOf(b.getBibliotecarioId()), "ADMIN");
                        BibliotecarioDTO bibliotecarioDTO = new BibliotecarioDTO(
                                b.getBibliotecarioId(),
                                b.getNome(),
                                b.getEmail()
                        );
                        return new TokenDTO(token, "ADMIN", bibliotecarioDTO);

                    });
        }

        public Optional<BibliotecarioDTO> buscarPorEmail(String email) {
            return bibliotecarioRepository.findByEmail(email)
                    .map(BibliotecarioDTO::new);
        }
    }
