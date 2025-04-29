package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/alunos/autenticar","/alunos/salvar").permitAll() // Permitir autenticação de alunos
                        .requestMatchers("/bibliotecarios/autenticar").permitAll() // Permitir autenticação de bibliotecários
                        .requestMatchers("/bibliotecarios/livros/**").hasRole("ADMIN") // Apenas administradores podem gerenciar livros
                        .requestMatchers("/alunos/**", "/bibliotecarios/**").hasRole("USER") // Apenas usuários autenticados podem acessar dados
                        .anyRequest().authenticated() // Qualquer outra requisição precisa estar autenticada
                )
                .logout(logout -> logout.permitAll()) // Permitir logout
                .build();
    }
}
