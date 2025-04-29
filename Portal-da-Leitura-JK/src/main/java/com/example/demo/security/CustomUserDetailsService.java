package com.example.demo.security;

import com.example.demo.service.AlunoService;
import com.example.demo.service.BibliotecarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AlunoService alunoService;
    private final BibliotecarioService bibliotecarioService;

    public CustomUserDetailsService(AlunoService alunoService, BibliotecarioService bibliotecarioService) {
        this.alunoService = alunoService;
        this.bibliotecarioService = bibliotecarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = alunoService.loadUserByUsername(username);

        if (user == null) {
            user = bibliotecarioService.loadUserByUsername(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return user;
    }
}

