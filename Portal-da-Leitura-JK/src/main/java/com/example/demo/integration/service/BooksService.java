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

    // Corrigido: Agora retorna uma lista de LivroModel
    public List<LivroModel> buscarLivros(String query) {
        List<BooksDTO> livrosDTO = booksClient.buscarLivros(query);

        // Convertendo BooksDTO para LivroModel antes de retornar
        return livrosDTO.stream()
                .map(dto -> new LivroModel(
                        null,  // ID gerado pelo banco de dados
                        dto.getTitulo(),
                        dto.getAutor(),
                        dto.getGenero(),
                        dto.getDescricao(),
                        dto.getAnoPublicacao(),
                        dto.getCurso(),
                        dto.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

    public List<LivroModel> salvarLivros(List<BooksDTO> livrosDTO) {
        List<LivroModel> livros = livrosDTO.stream()
                .map(dto -> new LivroModel(
                        null,
                        dto.getTitulo(),
                        dto.getAutor(),
                        dto.getGenero(),
                        dto.getDescricao(),
                        dto.getAnoPublicacao(),
                        dto.getCurso(),
                        dto.getQuantidade()
                ))
                .collect(Collectors.toList());

        return livroRepository.saveAll(livros);
    }
}
