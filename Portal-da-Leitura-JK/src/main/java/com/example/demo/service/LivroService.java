package com.example.demo.service;

import com.example.demo.model.AvaliacaoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.repository.AvaliacaoRepository;
import com.example.demo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<LivroModel> listarLivros() {
        return livroRepository.findAll();
    }

    public List<LivroModel> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<LivroModel> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<LivroModel> buscarPorGenero(String genero) {
        return livroRepository.findByGeneroContainingIgnoreCase(genero);
    }

    public List<LivroModel> buscarPorCurso(String curso) {
        return livroRepository.findByCursoContainingIgnoreCase(curso);
    }

    public List<LivroModel> buscarPorEditora(String editora) {
        return livroRepository.findByEditoraContainingIgnoreCase(editora);
    }

    public List<LivroModel> filtrarLivros(String titulo, String autor, String genero, String editora, String curso) {
        titulo = (titulo == null) ? "" : titulo;
        autor = (autor == null) ? "" : autor;
        genero = (genero == null) ? "" : genero;
        editora = (editora == null) ? "" : editora;
        curso = (curso == null) ? "" : curso;

        return livroRepository.findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCaseAndGeneroContainingIgnoreCaseAndEditoraContainingIgnoreCaseAndCursoContainingIgnoreCase(
                titulo, autor, genero, editora, curso);
    }

    public boolean estaDisponivel(Long livroId) {
        Optional<LivroModel> livro = livroRepository.findById(livroId);
        return livro.isPresent() && livro.get().getQuantidade() > 0;
    }

    public LivroModel reduzirEstoque(Long livroId) {
        Optional<LivroModel> livro = livroRepository.findById(livroId);
        if (livro.isPresent() && livro.get().getQuantidade() > 0) {
            LivroModel livroAtualizado = livro.get();
            livroAtualizado.setQuantidade(livroAtualizado.getQuantidade() - 1);
            return livroRepository.save(livroAtualizado);
        }
        return null;
    }

    public LivroModel aumentarEstoque(Long livroId, Integer quantidade) {
        Optional<LivroModel> livro = livroRepository.findById(livroId);
        if (livro.isPresent()) {
            LivroModel livroAtualizado = livro.get();
            livroAtualizado.setQuantidade(livroAtualizado.getQuantidade() + quantidade);
            return livroRepository.save(livroAtualizado);
        }
        return null;
    }

    public double getMediaAvaliacao(Long livroId) {
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByLivroLivroId(livroId);
        if (avaliacoes.isEmpty()) {
            return 0;
        }
        double somaNotas = 0;
        for (AvaliacaoModel avaliacao : avaliacoes) {
            somaNotas += avaliacao.getNota();
        }
        return somaNotas / avaliacoes.size();
    }

    public List<AvaliacaoModel> listarAvaliacoes(Long livroId) {
        return avaliacaoRepository.findByLivroLivroId(livroId);
    }
}
