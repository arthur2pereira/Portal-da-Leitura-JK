package com.example.demo.repository;

import com.example.demo.model.BibliotecarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BibliotecarioRepository extends JpaRepository<BibliotecarioModel, Long> {
    Optional<BibliotecarioModel> findByEmail(String email);
    Optional<BibliotecarioModel> findByBibliotecarioId (Long bibliotecarioId);
    boolean existsByEmail(String email);
}

