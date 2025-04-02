package com.example.demo.service;

import com.example.demo.model.LivroModel;
import com.example.demo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

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

    public LivroModel salvar(LivroModel livro) {
        return livroRepository.save(livro);
    }

    public Optional<LivroModel> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }
}
