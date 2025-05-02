package com.example.demo.security;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final AlunoRepository alunoRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    public CustomUserDetailsService(AlunoRepository alunoRepo, BibliotecarioRepository biblioRepo) {
        this.alunoRepository = alunoRepo;
        this.bibliotecarioRepository = biblioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AlunoModel> alunoOpt = alunoRepository.findByEmail(username);
        if (alunoOpt.isEmpty()) {
            alunoOpt = alunoRepository.findByMatricula(username);
        }

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            return User.builder()
                    .username(aluno.getEmail())
                    .password(aluno.getSenha())
                    .roles("USER")
                    .build();
        }

        Optional<BibliotecarioModel> biblioOpt = bibliotecarioRepository.findByEmail(username);
        if (biblioOpt.isPresent()) {
            BibliotecarioModel biblio = biblioOpt.get();
            return User.builder()
                    .username(biblio.getEmail())
                    .password(biblio.getSenha())
                    .roles("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}

