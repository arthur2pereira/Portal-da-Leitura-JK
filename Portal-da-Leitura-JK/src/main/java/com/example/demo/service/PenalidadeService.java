package com.example.demo.service;

import com.example.demo.model.PenalidadeModel;
import com.example.demo.repository.PenalidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenalidadeService {

    @Autowired
    private PenalidadeRepository penalidadeRepository;

    public List<PenalidadeModel> buscarPorAluno(Long matricula) {
        return penalidadeRepository.findByMatricula(matricula);
    }

    public List<PenalidadeModel> buscarPorTipo(String tipo) {
        return penalidadeRepository.findByTipo(tipo);
    }

    public PenalidadeModel salvar(PenalidadeModel penalidade) {
        return penalidadeRepository.save(penalidade);
    }
}
