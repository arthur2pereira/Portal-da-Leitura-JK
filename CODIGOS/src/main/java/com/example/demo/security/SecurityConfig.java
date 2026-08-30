package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(customUserDetailsService)
                   .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // ----------------- ROTAS PÚBLICAS -----------------
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/alunos/autenticar",
                                "/alunos/salvar",
                                "/bibliotecarios/autenticar"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/alunos/esqueci-senha",
                                "/alunos/redefinir-senha"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/avaliacoes/mais-avaliados",
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
                                "/livros/avaliacoes/**", 
                                "/livros/**"
                        ).permitAll()
                        
                        .requestMatchers(HttpMethod.POST,"/alunos/reativar/**").permitAll()

                        // ----------------- ROTAS USER (aluno logado) -----------------
                        .requestMatchers("/alunos/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/avaliacoes/**").hasRole("USER")
                        .requestMatchers("/avaliacoes/**").hasRole("USER")
                        .requestMatchers("/reservas/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/emprestimos/*/renovar").hasRole("USER")

                        // gerenciamento da própria conta
                        .requestMatchers(HttpMethod.POST,
                                "/alunos/desativar/**",
                                "/alunos/excluir/**"
                        ).hasRole("USER")
                        

                        // ----------------- ROTAS ADMIN -----------------
                        .requestMatchers("/bibliotecarios/**").hasRole("ADMIN")
                        .requestMatchers("/bibliotecarios/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/emprestimos/*/renovar-admin").hasRole("ADMIN")
                        .requestMatchers("/emprestimos/*/devolver").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/emprestimos/*/registrarEmprestimo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/alunos/processar-exclusoes").hasRole("ADMIN")

                        // operações de CRUD em livros só admin
                        .requestMatchers(HttpMethod.POST, "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/livros/**").hasRole("ADMIN")

                        // ----------------- FALLBACK -----------------
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
