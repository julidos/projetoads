package com.projeto.ads.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.ads.model.Usuario;
import com.projeto.ads.repository.RoleRepository;
import com.projeto.ads.repository.UsuarioRepository;
import com.projeto.ads.service.ServiceEmail;
import com.projeto.ads.util.Util;



@Controller
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private ServiceEmail serviceEmail;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("usuario", new Usuario());
        mv.setViewName("Login/login");
        return mv;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashBoard(Authentication authentication) {
        ModelAndView mv = new ModelAndView();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        int atIndex = username.indexOf("@");
        String nameUser = atIndex != -1 ? username.substring(0, atIndex): username;
        mv.addObject("usuario", nameUser);
        mv.addObject("papel", role);
        mv.setViewName("Login/index");
        return mv;
    }

    @GetMapping("usuario/inserir")
    public ModelAndView cadastro() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("roles", roleRepository.findAll());
        mv.addObject("usuario", new Usuario());
        mv.setViewName("Login/cadastro");
        return mv;
    }

    @PostMapping("usuario/inserir")
    public ModelAndView salvarUsuario(
        @ModelAttribute Usuario user, 
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam("dataNasc") String dataNasc,
        @RequestParam("roleUser") String roleUser
    ) {
        ModelAndView mv = new ModelAndView();

        if(user.getPassword().length() < 8) {
            mv.setViewName("Login/cadastro");
            mv.addObject("error", "A senha possuí menos de 8 dígitos");
            mv.addObject("roles", roleRepository.findAll());
            return mv;
        }

        if(!user.getPassword().equals(confirmPassword)) {
            mv.setViewName("Login/cadastro");
            mv.addObject("error", "As senhas não correspondem");
            mv.addObject("roles", roleRepository.findAll());
            return mv;
        }
        Date dataFormatada = null;

        try {
            SimpleDateFormat formataEntrada = new SimpleDateFormat("yyyy-MM-dd");
            dataFormatada = formataEntrada.parse(dataNasc);
        } catch (ParseException error) {
            error.printStackTrace();
        }

        user.setDataNascimento(dataFormatada);
        user.setUsername(user.getEmail());

        Usuario aux = userRepository.findByUsername(user.getUsername());

        if(aux == null) {
            user.setRole(roleRepository.findByNome(roleUser));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
    
            userRepository.save(user);
    
            mv.setViewName("redirect:/login");
            
            return mv;
        } else {
            mv.setViewName("Login/cadastro");
            mv.addObject("error", "Usuário já foi cadastrado");
            mv.addObject("roles", roleRepository.findAll());
            return mv;
        }
    }

    @GetMapping("/usuario/recuperar")
    public ModelAndView recuperarSenha() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("usuario", new Usuario());
        mv.setViewName("Login/recuperar");
        return mv;
    }

    @PostMapping("/usuario/recuperar")
    public ModelAndView recuperarSenha(@ModelAttribute Usuario usuario, 
    									RedirectAttributes redirect) {
        ModelAndView mv = new ModelAndView();
        Usuario aux = userRepository.findByEmail(usuario.getEmail());
        if (aux == null) {
            mv.addObject("usuario", new Usuario());
            mv.setViewName("Login/recuperar");
            mv.addObject("error", "Email não encontrado");
            return mv;
        }
        aux.setToken(Util.generateToken());
        userRepository.save(aux);
        String mensagem="use esse token para recuperar sua senha:"+aux.getToken();
        //serviceEmail.sendEmail("senaclpoo@gmail.com", aux.getEmail(), mensagem, "Senac informa:Recupere senha plataforma gestão de aluno");
        usuario.setToken("");
        mv.addObject("usuario", aux);
        redirect.addFlashAttribute("usuario", usuario);
        mv.setViewName("redirect:/usuario/atualizarUsuario");
        return mv;
    }
    	@GetMapping("/usuario/atualizarUsuario")
    	public ModelAndView updatePass (Usuario usuario) {
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("usuario", usuario);
    		mv.setViewName("Login/atualizar");
    		return mv;
    	}
    	@PostMapping("/usuario/atualizarUsuario")
    	public ModelAndView updatePassPost (@ModelAttribute Usuario usuario,
    										@RequestParam("confirmPassword") String confirmPassword,
    										@RequestParam("email") String email) {
    		ModelAndView mv = new ModelAndView();
    		Usuario aux= userRepository.findByEmail(email);
    		if(!aux.getToken().equals(usuario.getToken())) {
    			mv.setViewName("Login/atualizar");
    			mv.addObject("error","token não confere");
    			mv.addObject("usuario", usuario);
    			return mv;
    		}
    		if (usuario.getPassword().equals(confirmPassword)|| confirmPassword.length()<8) {
    		String erro= confirmPassword.length()<8? "Senha precisa ter no minimo tamanho 8!":"Senhas não conferem!";
    		mv.setViewName("Login/atualizar");
			mv.addObject("error","erro");
			mv.addObject("usuario", usuario);
			return mv;
    		}
    		aux.setToken("");
    		aux.setPassword(passwordEncoder.encode(confirmPassword));
    		userRepository.save(aux);
    		mv.addObject("usuario", new Usuario());
    		mv.setViewName("redirect:/login");
    		
    		return mv;
    	}

}


