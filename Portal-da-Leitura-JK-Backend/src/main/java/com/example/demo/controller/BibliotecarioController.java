package com.example.demo.controller;

import com.example.demo.model.BibliotecarioModel;
import com.example.demo.service.BibliotecarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {

    @Autowired
    private BibliotecarioService bibliotecarioService;

    @GetMapping("/{email}")
    public ResponseEntity<BibliotecarioModel> buscarPorEmail(@PathVariable String email) {
        Optional<BibliotecarioModel> bibliotecario = bibliotecarioService.buscarPorEmail(email);
        return bibliotecario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<BibliotecarioModel> salvar(@RequestBody BibliotecarioModel bibliotecario) {
        BibliotecarioModel novoBibliotecario = bibliotecarioService.salvar(bibliotecario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBibliotecario);
    }
}
