package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permissões públicas
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/alunos/autenticar", "/alunos/salvar").permitAll()
                        .requestMatchers("/bibliotecarios/autenticar").permitAll()

                        // Acesso aluno (protegido)
                        .requestMatchers("/alunos/**").hasRole("USER")
                        .requestMatchers("/livros/avaliacoes/**").hasRole("USER")

                        // Acesso bibliotecário (protegido)
                        .requestMatchers("/bibliotecarios/livros/**").hasRole("ADMIN")
                        .requestMatchers("/bibliotecarios/**").hasRole("ADMIN")

                        // Rotas públicas de livros (GET apenas)
                        .requestMatchers(HttpMethod.GET,
                                "/livros/listar",
                                "/livros/buscar",
                                "/livros/titulo/**",
                                "/livros/autor/**",
                                "/livros/genero/**",
                                "/livros/curso/**",
                                "/livros/editora/**",
                                "/livros/disponivel/**",
                                "/livros/avaliacao/media/**",
                                "/livros/titulos",
                                "/livros/cursos",
                                "/livros/generos",
                                "/livros/editoras",
                                "/livros/autores",
                                "/livros/**"
                        ).permitAll()

                        // As outras operações (POST, PUT, DELETE) em /livros/** são para ADMIN
                        .requestMatchers(HttpMethod.POST, "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/livros/**").hasRole("ADMIN")

                        // O restante exige autenticaçãov
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.permitAll())
                .build();
    }
}
