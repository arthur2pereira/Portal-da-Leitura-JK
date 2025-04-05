package com.example.demo.repository;

import com.example.demo.model.BibliotecarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BibliotecarioRepository extends JpaRepository<BibliotecarioModel, Long> {
    Optional<BibliotecarioModel> findByEmail(String email);
    boolean existsByEmail(String email);
}

