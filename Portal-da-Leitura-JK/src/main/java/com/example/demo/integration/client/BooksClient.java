package com.example.demo.integration.client;

import com.example.demo.integration.dto.BooksDTO;
import com.example.demo.integration.exception.BooksException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BooksClient {
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public List<BooksDTO> buscarLivros(String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_BOOKS_API_URL + query;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || !responseBody.containsKey("items")) {
            throw new BooksException("Nenhum livro encontrado para a busca: " + query);
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");
        List<BooksDTO> livros = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");

            String titulo = (String) volumeInfo.get("title");
            List<String> autores = (List<String>) volumeInfo.get("authors");
            String autor = (autores != null && !autores.isEmpty()) ? String.join(", ", autores) : "Desconhecido";
            List<String> categorias = (List<String>) volumeInfo.get("categories");
            String genero = (categorias != null && !categorias.isEmpty()) ? categorias.get(0) : "Gênero desconhecido";
            String descricao = (String) volumeInfo.getOrDefault("description", "Sem descrição disponível");
            String dataPublicacao = (String) volumeInfo.get("publishedDate");
            Integer anoPublicacao = (dataPublicacao != null && dataPublicacao.length() >= 4) ? Integer.parseInt(dataPublicacao.substring(0, 4)) : null;

            // Cursos predefinidos
            String curso = definirCurso(genero);

            BooksDTO livroDTO = new BooksDTO(null, titulo, autor, genero, descricao, anoPublicacao, curso,null);
            livros.add(livroDTO);
        }

        return livros;
    }

    private String definirCurso(String genero) {
        switch (genero.toLowerCase()) {
            case "tecnologia":
            case "informática":
                return "Informática";
            case "biologia":
            case "medicina":
            case "saúde":
                return "Análises Clínicas";
            case "administração":
            case "negócios":
                return "Administração";
            case "turismo":
            case "viagens":
                return "Guia de Turismo";
            case "engenharia":
            case "elétrica":
            case "eletrônica":
                return "Eletrotécnica";
            default:
                return "Curso Indefinido";
        }
    }
}
