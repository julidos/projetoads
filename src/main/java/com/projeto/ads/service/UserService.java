package com.projeto.ads.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.ads.model.Usuario;
import com.projeto.ads.repository.UsuarioRepository;

@Service
public class UserService {

        @Autowired
        UsuarioRepository userRepository;

        public String validaErros(Usuario user, String confirmPassword, String dataNascimentoString) {
                String error = "";
                if (!user.getPassword().equals(confirmPassword)) {
                        error += "As senhas não correspondem!";
                        return error;
                }
                if (user.getPassword().length() < 8) {
                        error += "A senha precisa ter no mínimo 8 caracteres!";
                        return error;
                }
                String email = user.getEmail();
                Usuario retorno = userRepository.findByEmail(email);
                if (retorno != null) {
                        error += "Esse email já está cadastrado na aplicação!";
                        return error;
                }
                if (!this.data(dataNascimentoString)) {
                        error += "Data de nascimento é inválida!";
                        return error;
                }
                return error;
        }

        public boolean data(String dataNasc) {
                LocalDate hoje = LocalDate.now();
                int dia = hoje.getDayOfMonth();
                int mes = hoje.getMonthValue();
                int ano = hoje.getYear();
                String[] partes = dataNasc.split("-");
                int anoNasc = Integer.parseInt(partes[0]);
                int mesNasc = Integer.parseInt(partes[1]);
                int diaNasc = Integer.parseInt(partes[2]);
                if (anoNasc < ano) {
                        return true;
                } else {
                        if (mesNasc < mes) {
                                return true;
                        } else {
                                if (diaNasc <= dia) {
                                        return true;
                                }
                        }
                }
                return false;
        }
}