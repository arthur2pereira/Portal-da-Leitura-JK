package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.model.AlunoModel;
import com.example.demo.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Optional<AlunoModel> buscarPorMatricula(String matricula) {
        return alunoRepository.findByMatricula(matricula);
    }

    public List<AlunoModel> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public boolean alunoExiste(String matricula) {
        return alunoRepository.existsByMatricula(matricula);
    }

    public AlunoModel salvar(AlunoModel aluno) {
        return alunoRepository.save(aluno);
    }

    // Se quiser salvar com DTO direto (opcional)
    public AlunoModel salvarDTO(AlunoDTO dto) {
        AlunoModel aluno = new AlunoModel(
                dto.getMatricula(),
                dto.getNome(),
                dto.getEmail(),
                dto.getSenha(),
                dto.getStatus().equalsIgnoreCase("Ativo")
        );
        return alunoRepository.save(aluno);
    }

    public AlunoDTO converterParaDTO(AlunoModel aluno) {
        return new AlunoDTO(
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getSenha(),
                aluno.getStatus() ? "Ativo" : "Inativo"
        );
    }

    public Optional<AlunoModel> autenticar(String matricula, String senha) {
        Optional<AlunoModel> alunoOpt = alunoRepository.findByMatricula(matricula);

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            if (aluno.getSenha().equals(senha)) {
                return Optional.of(aluno);
            }
        }

        return Optional.empty();
    }

}
