package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PenalidadeService {

    @Autowired
    private PenalidadeRepository penalidadeRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public List<PenalidadeModel> buscarPorAluno(String matricula) {
        return penalidadeRepository.findByAlunoMatricula(matricula);
    }

    public List<PenalidadeModel> buscarPorTipo(String tipo) {
        return penalidadeRepository.findByTipoIgnoreCase(tipo);
    }

    public PenalidadeModel aplicarPenalidade(String matricula, String motivo, String tipo, Integer diasBloqueio, String email) {
        AlunoModel aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));

        BibliotecarioModel bibliotecario = bibliotecarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliotecário não encontrado"));

        PenalidadeModel penalidade = new PenalidadeModel();
        penalidade.setAluno(aluno);
        penalidade.setMotivo(motivo);
        penalidade.setTipo(tipo);
        penalidade.setDataAplicacao(LocalDate.now());
        penalidade.setDiasBloqueio(diasBloqueio);

        return penalidadeRepository.save(penalidade);
    }

    public boolean estaAtiva(Long penalidadeId) {
        PenalidadeModel p = penalidadeRepository.findByPenalidadeId(penalidadeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penalidade não encontrada"));
        LocalDate dataFim = p.getDataAplicacao().plusDays(p.getDiasBloqueio());
        return LocalDate.now().isBefore(dataFim);
    }

    public int diasRestantes(Long penalidadeId) {
        PenalidadeModel p = penalidadeRepository.findByPenalidadeId(penalidadeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Penalidade não encontrada"));
        LocalDate dataFim = p.getDataAplicacao().plusDays(p.getDiasBloqueio());
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), dataFim);
        return (dias > 0) ? (int) dias : 0;
    }

    public List<AvaliacaoModel> listarAvaliacoes(String matriculaAluno) {
        return avaliacaoRepository.findByAlunoMatricula(matriculaAluno);
    }

    private PenalidadeDTO toDTO(PenalidadeModel penalidade) {
        return new PenalidadeDTO(
                penalidade.getPenalidadeId(),
                penalidade.getAluno().getMatricula(),
                penalidade.getMotivo(),
                penalidade.getTipo(),
                penalidade.getDataAplicacao(),
                penalidade.getDiasBloqueio()
        );
    }
}
