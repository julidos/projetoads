package com.projeto.ads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/aluno/listar")
	public ModelAndView listarAluno() {
		ModelAndView mv = new ModelAndView();
		 mv.addObject("alunos", alunoRepository.findAllOrderedById());
		 mv.setViewName("Aluno/listarAluno");
		 return mv;
	}
	
	@GetMapping("/aluno/alterar/{id}")
	public ModelAndView alterarAluno(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView();
		Aluno aluno = alunoRepository.findById(id).get();
		mv.addObject("aluno",aluno);
		mv.setViewName("Aluno/alterarAluno");
		return mv;
	}
	
	@GetMapping("/aluno/editar")
	public ModelAndView alterarAluno() {
		ModelAndView mv = new ModelAndView();
		 mv.addObject("alunos", alunoRepository.findAllOrderedById());
		 mv.setViewName("Aluno/listarEditarAluno");
		 return mv;
	}
	
	@GetMapping("/aluno/deletar")
	public ModelAndView excluirAluno() {
		ModelAndView mv = new ModelAndView();
		 mv.addObject("alunos", alunoRepository.findAllOrderedById());
		 mv.setViewName("Aluno/listarDeletarAluno");
		 return mv;
	}
	
	@PostMapping("/aluno/alterar")
	public ModelAndView alterarAluno(Aluno aluno) {
		ModelAndView mv = new ModelAndView();
		String out = serviceAluno.alterarAluno(aluno, aluno.getId());
		 if (out!=null) {
			 mv.addObject("msg",out);
			 mv.addObject("aluno", aluno);
			 mv.setViewName("Aluno/alterarAluno");
		 } else {
			 mv.setViewName("redirect:/aluno/listar");
		 }
		return mv;
	}
	
	@GetMapping("/aluno/excluir/{id}")
	public String excluirAluno(@PathVariable("id") Long id) {
		alunoRepository.deleteById(id);
		return "redirect:/aluno/listar";
	}
}
