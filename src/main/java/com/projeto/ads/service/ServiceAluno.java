package com.projeto.ads.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.ads.model.Aluno;
import com.projeto.ads.repository.AlunoRepository;

@Service
public class ServiceAluno {

        @Autowired
        AlunoRepository alunoRepository;
        public String verificarAluno(String cpf) {
                Aluno aux = alunoRepository.findByCpf(cpf);

                if (aux != null) {
                        return "Já existe um aluno com esse cpf!";
                } else {
                        return null;
                }
        }

        public String gerarMat() {
                Date data = new Date();
                Calendar calendario = Calendar.getInstance();
                calendario.setTime(data);
                int ano = calendario.get(Calendar.YEAR);
                Aluno aluno = alunoRepository.findLastInsertedAluno();

                if (aluno == null) {
                        return ano + "1";
                } else {
                        String out = ano + "";
                        return out + (aluno.getId() + 1);
                }

        }

        public String alterarAluno(Aluno aluno, Long id) {
                Aluno alunoExistente = alunoRepository.findByCpf(aluno.getCpf());
                if ((alunoExistente != null && alunoExistente.getId() == id) || alunoExistente == null) {
                        alunoRepository.save(aluno);
                } else {
                        return "Já existe um aluno cadastrado com o mesmo CPF !";
                }

                return null;
        }
}