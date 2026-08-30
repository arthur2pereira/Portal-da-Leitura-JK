package com.example.demo.repository;

import com.example.demo.model.ReseteSenhaToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReseteSenhaTokenRepository extends JpaRepository<ReseteSenhaToken, Long> {
    Optional<ReseteSenhaToken> findByToken(String token);
}