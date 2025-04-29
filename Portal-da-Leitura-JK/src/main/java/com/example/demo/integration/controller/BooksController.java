package com.example.demo.integration.controller;

import com.example.demo.integration.service.BooksService;
import com.example.demo.model.LivroModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/google-books")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/buscar")
    public List<LivroModel> buscarLivros(@RequestParam String query) {
        return booksService.buscarLivros(query);
    }

    @GetMapping("/buscar-salvar-unico")
    public List<LivroModel> buscarSalvarSemDuplicar(@RequestParam String query) {
        return booksService.buscarSalvarSemDuplicar(query);
    }
}
