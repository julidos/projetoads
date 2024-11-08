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
		String error="";
		if(!user.getPassword().equals(confirmPassword)) {
			error+="As senhas não correspondem!";
		}
		if(user.getPassword().length() < 8) {
			error+="A senha precisa ter no mínimo 8 caracteres!";
			}
			String email = user.getEmail();
			Usuario retorno= userRepository.findByEmail(email);
			if(retorno !=null) {
			error+="Esse email já está cadastrado na aplicação!";
			}
			if(!this.data(dataNascimentoString)) {
			error+="Data de nascimento é inválida!";
			}
			return error;
			} 

			public boolean data(String dataNasc) {
			LocalDate hoje = LocalDate.now();
			LocalDate dataNascimento = LocalDate.parse(dataNasc);
			return dataNascimento.isBefore(hoje);
			}
	}
