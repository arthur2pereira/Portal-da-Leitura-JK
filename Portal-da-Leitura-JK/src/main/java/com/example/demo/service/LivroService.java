package com.example.demo.service;

import com.example.demo.dto.LivroDTO;
import com.example.demo.model.AvaliacaoModel;
import com.example.demo.model.LivroModel;
import com.example.demo.repository.AvaliacaoRepository;
import com.example.demo.repository.EmprestimoRepository;
import com.example.demo.repository.LivroRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public Page<LivroModel> listarLivrosPaginados(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return livroRepository.findAll(pageable);
    }

    public LivroModel buscarPorLivroId(Long livroId) {
        return livroRepository.findByLivroId(livroId)
                .orElse(null);
    }

    public List<String> listarCursos() {
        return livroRepository.findAllCursosUnicos();
    }

    public List<String> listarAutores() {
        return livroRepository.findAllAutoresUnicos();
    }

    public List<String> listarEditoras() {
        return livroRepository.findAllEditorasUnicos();
    }

    public List<String> listarGeneros() {
        return livroRepository.findAllGenerosUnicos();
    }

    public Page<LivroModel> buscarComFiltros(String titulo, String autor, String genero, String curso, String editora, Pageable pageable) {
        Specification<LivroModel> spec = Specification.where(null);

        if (StringUtils.hasText(titulo)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(autor)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("autor")), "%" + autor.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(genero)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("genero")), "%" + genero.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(curso)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("curso")), "%" + curso.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(editora)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("editora")), "%" + editora.toLowerCase() + "%"));
        }

        return livroRepository.findAll(spec, pageable);
    }

    public Page<LivroModel> buscarPorTituloPaginado(String titulo, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return livroRepository.findByTituloContainingIgnoreCase(titulo, pageable);
    }

    public LivroModel salvarLivro(LivroDTO livroDTO) {
        LivroModel livro = new LivroModel();
        livro.setTitulo(livroDTO.getTitulo());
        livro.setAutor(livroDTO.getAutor());
        livro.setGenero(livroDTO.getGenero());
        livro.setCurso(livroDTO.getCurso());
        livro.setEditora(livroDTO.getEditora());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setDescricao(livroDTO.getDescricao());
        livro.setQuantidade(livroDTO.getQuantidade());
        return livroRepository.save(livro);
    }

    public boolean estaDisponivel(Long livroId) {
        Optional<LivroModel> livro = livroRepository.findByLivroId(livroId);
        return livro.isPresent() && livro.get().getQuantidade() > 0;
    }

    public LivroModel reduzirEstoque(Long livroId) {
        Optional<LivroModel> livro = livroRepository.findByLivroId(livroId);
        if (livro.isPresent() && livro.get().getQuantidade() > 0) {
            LivroModel livroAtualizado = livro.get();
            if (livroAtualizado.getQuantidade() > 0) {
                livroAtualizado.setQuantidade(livroAtualizado.getQuantidade() - 1);
                return livroRepository.save(livroAtualizado);
            }
        }
        throw new IllegalStateException("Estoque insuficiente");
    }

    public LivroModel aumentarEstoque(Long livroId, Integer quantidade) {
        Optional<LivroModel> livro = livroRepository.findById(livroId);
        if (livro.isPresent()) {
            LivroModel livroAtualizado = livro.get();
            livroAtualizado.setQuantidade(livroAtualizado.getQuantidade() + quantidade);
            return livroRepository.save(livroAtualizado);
        }
        return null;
    }

    public int getQuantidadeDisponivel(Long livroId) {
        LivroModel livro = livroRepository.findById(livroId).orElse(null);
        if (livro == null) {
            return 0;
        }

        int total = livro.getQuantidade();
        int reservadas = reservaRepository.countByLivroAndStatusTrue(livro);
        int emprestadas = emprestimoRepository.countByLivroAndDataDevolucaoIsNull(livro);

        int disponivel = total - reservadas - emprestadas;

        return Math.max(disponivel, 0);
    }

    public double getMediaAvaliacao(Long livroId) {
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByLivroLivroId(livroId);
        if (avaliacoes.isEmpty()) {
            return 0;
        }
        double somaNotas = 0;
        for (AvaliacaoModel avaliacao : avaliacoes) {
            somaNotas += avaliacao.getNota();
        }
        return somaNotas / avaliacoes.size();
    }

    public List<AvaliacaoModel> listarAvaliacoes(Long livroId) {
        return avaliacaoRepository.findByLivroLivroId(livroId);
    }
}
