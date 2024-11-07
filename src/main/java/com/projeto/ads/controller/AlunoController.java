package com.projeto.ads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.ads.model.Aluno;
import com.projeto.ads.repository.AlunoRepository;
import com.projeto.ads.service.ServiceAluno;

@Controller
public class AlunoController {
	
	@Autowired
	ServiceAluno serviceAluno;

	@Autowired
	AlunoRepository alunoRepository;
	
	@GetMapping("/aluno/inserir")
	public ModelAndView insertAlunoGet() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("aluno", new Aluno());
		mv.setViewName("Aluno/inserirAluno");
		return mv;
	}
	
	@PostMapping("/aluno/inserir")
	public ModelAndView insertAlunoPost(@ModelAttribute Aluno aluno) {
	 ModelAndView mv = new ModelAndView();
	 
	 String a= serviceAluno.verificarAluno(aluno.getCpf());
	 if (a!=null) {
		 mv.addObject("aluno", new Aluno());
		 mv.setViewName("Aluno/inserirAluno");
		 mv.addObject("msg",a);
		 return mv;
	 } else {
		 aluno.setMatricula(serviceAluno.gerarMat());
		 alunoRepository.save(aluno);
		 mv.setViewName("redirect:/aluno/listar");
	 }
	 
	 return mv;
	}
}
