package com.example.demo.service;

import com.example.demo.model.BibliotecarioModel;
import com.example.demo.repository.BibliotecarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BibliotecarioService {

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    public Optional<BibliotecarioModel> buscarPorEmail(String email) {
        return bibliotecarioRepository.findByEmail(email);
    }

    public BibliotecarioModel salvar(BibliotecarioModel bibliotecario) {
        return bibliotecarioRepository.save(bibliotecario);
    }

    public Optional<BibliotecarioModel> buscarPorId(Long id) {
        return bibliotecarioRepository.findById(id);
    }
}
