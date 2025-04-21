package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecarioService {

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

    public LivroModel salvar(LivroModel livro) {
        return livroRepository.save(livro);
    }

    public Optional<LivroModel> buscarPorlivroId(Long livroId) {
        return livroRepository.findByLivroId(livroId);
    }

    public boolean remover(Long livroId) {
        Optional<LivroModel> livroOpt = livroRepository.findById(livroId);
        if (livroOpt.isPresent()) {
            livroRepository.delete(livroOpt.get());
            return true;
        }
        return false;
    }

    public List<ReservaModel> buscarTodasReservas() {
        return reservaRepository.findAll();
    }

    public List<PenalidadeModel> buscarPenalidadesPorMatricula(String matricula) {
        return penalidadeRepository.findByAlunoMatricula(matricula);
    }

    public List<EmprestimoModel> buscarEmprestimosPorMatricula(String matricula) {
        return emprestimoRepository.findByAlunoMatricula(matricula);
    }

    public boolean moderarComentario(Long avalicacaoId) {
        Optional<AvaliacaoModel> avaliacaoOpt = avaliacaoRepository.findByAvaliacaoId(avalicacaoId);

        if (avaliacaoOpt.isPresent()) {
            AvaliacaoModel avaliacao = avaliacaoOpt.get();
            avaliacaoRepository.delete(avaliacao);
            return true;
        }
        return false;
    }

    public Optional<BibliotecarioModel> autenticar(String email, String senha) {

        List<String> emailsPermitidos = List.of("bibliotecarioetejk@gmail.com", "bibliotecarioetejk2@gmail.com");
        if (!emailsPermitidos.contains(email)) {
            return Optional.empty();
        }

        Optional<BibliotecarioModel> bibliotecarioOpt = bibliotecarioRepository.findByEmail(email);
        System.out.println("Email buscado: " + email);

        if (bibliotecarioOpt.isPresent()) {
            BibliotecarioModel bibliotecario = bibliotecarioOpt.get();
            System.out.println("Senha armazenada: " + bibliotecario.getSenha());
            if (bibliotecario.getSenha().equals(senha)) {
                return Optional.of(bibliotecario);
            }
        }
        return Optional.empty();
    }

    public Optional<BibliotecarioModel> buscarPorEmail(String email) {
        return bibliotecarioRepository.findByEmail(email);
    }
}
