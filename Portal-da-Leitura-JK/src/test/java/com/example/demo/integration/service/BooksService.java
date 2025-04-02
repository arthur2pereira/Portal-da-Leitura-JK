package com.example.demo.integration.service;

import com.example.demo.integration.client.BooksClient;
import com.example.demo.integration.dto.BooksDTO;
import com.example.demo.repository.LivroRepository;
import com.example.demo.model.LivroModel;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksService {
    private final BooksClient booksClient;
    private final LivroRepository livroRepository;

    public BooksService(BooksClient booksClient, LivroRepository livroRepository) {
        this.booksClient = booksClient;
        this.livroRepository = livroRepository;
    }

    public List<BooksDTO> buscarLivros(String query) {
        return booksClient.buscarLivros(query);
    }

    public List<LivroModel> salvarLivros(List<BooksDTO> livrosDTO) {
        List<LivroModel> livros = livrosDTO.stream()
                .map(dto -> new LivroModel(null, dto.getTitulo(), dto.getAutor(), dto.getGenero(), dto.getDescricao(),
                        dto.getAnoPublicacao(), dto.getCurso(), dto.getQuantidade()))
                .collect(Collectors.toList());

        return livroRepository.saveAll(livros);
    }
}
