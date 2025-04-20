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

    public List<LivroModel> buscarLivros(String query) {
        List<BooksDTO> livrosDTO = booksClient.buscarLivros(query);

        return livrosDTO.stream()
                .map(dto -> new LivroModel(
                        null,
                        dto.getTitulo(),
                        dto.getAutor(),
                        dto.getGenero(),
                        dto.getDescricao(),
                        dto.getEditora(),
                        dto.getAnoPublicacao(),
                        dto.getCurso(),
                        dto.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

    public List<LivroModel> buscarSalvarSemDuplicar(String query) {
        List<BooksDTO> livrosDTO = booksClient.buscarLivros(query);

        List<LivroModel> novosLivros = livrosDTO.stream()
                .filter(dto -> !livroRepository.existsByTituloAndAutor(dto.getTitulo(), dto.getAutor()))
                .map(dto -> new LivroModel(
                        null,
                        dto.getTitulo(),
                        dto.getAutor(),
                        dto.getGenero(),
                        dto.getDescricao(),
                        dto.getEditora(),
                        dto.getAnoPublicacao(),
                        dto.getCurso(),
                        dto.getQuantidade()
                ))
                .collect(Collectors.toList());

        return livroRepository.saveAll(novosLivros);
    }

    public List<LivroModel> salvarLivros(List<BooksDTO> livrosDTO) {
        List<LivroModel> livros = livrosDTO.stream()
                .map(dto -> new LivroModel(
                        null,
                        dto.getTitulo(),
                        dto.getAutor(),
                        dto.getGenero(),
                        dto.getDescricao(),
                        dto.getEditora(),
                        dto.getAnoPublicacao(),
                        dto.getCurso(),
                        dto.getQuantidade()
                ))
                .collect(Collectors.toList());

        return livroRepository.saveAll(livros);
    }
}
