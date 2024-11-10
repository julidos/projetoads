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

import com.projeto.ads.model.Role;
import com.projeto.ads.model.Usuario;
import com.projeto.ads.repository.RoleRepository;
import com.projeto.ads.repository.UsuarioRepository;
import com.projeto.ads.service.ServiceEmail;
import com.projeto.ads.service.UserService;
import com.projeto.ads.util.Util;

@Controller
public class UsuarioController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        UserService userService;

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
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                String role = userDetails.getAuthorities().iterator().next().getAuthority();
                int atIndex = username.indexOf("@");
                String nameUser = atIndex != -1 ? username.substring(0, atIndex) : username;
                mv.addObject("usuario", nameUser);
                mv.addObject("papel", role);
                mv.setViewName("Login/index");
                return mv;
        }

        @GetMapping("/usuario/inserir")
        public ModelAndView cadastro() {
                ModelAndView mv = new ModelAndView();
                mv.addObject("roles", roleRepository.findAll());
                mv.addObject("usuario", new Usuario());
                mv.setViewName("Login/cadastro");
                return mv;
        }

        @PostMapping("/usuario/inserir")
        public ModelAndView salvarUsuario(
                @ModelAttribute Usuario user,
                @RequestParam("confirmPassword") String confirmPassword,
                @RequestParam("dataNasc") String dataNascimentoString,
                @RequestParam("roleUser") String roleUser
        ) {
                ModelAndView mv = new ModelAndView();
                String error = userService.validaErros(user, confirmPassword, dataNascimentoString);
                if (error.length() > 1) {
                        mv.addObject("usuario", new Usuario());
                        mv.addObject("roles", roleRepository.findAll());
                        mv.addObject("error", error);
                        mv.setViewName("Login/cadastro");
                        return mv;
                }
                Date dataFormatada = null;

                try {
                        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                        dataFormatada = formatoEntrada.parse(dataNascimentoString);
                } catch (ParseException e) {
                        e.printStackTrace();
                }

                user.setDataNascimento(dataFormatada);
                Role role = roleRepository.findByNome(roleUser);
                user.setRole(role);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setUsername(user.getEmail());
                userRepository.save(user);
                mv.setViewName("redirect:/login");
                return mv;
        }

        @GetMapping("/usuario/recuperar")
        public ModelAndView recuperarSenha() {
                ModelAndView mv = new ModelAndView();
                mv.addObject("usuario", new Usuario());
                mv.setViewName("Login/recuperar");
                return mv;
        }

        @PostMapping("/usuario/recuperar")
        public ModelAndView recuperarSenha(Usuario user, RedirectAttributes redirectAttributes) throws Exception {
                ModelAndView mv = new ModelAndView();
                Usuario aux = userRepository.findByEmail(user.getEmail());
                if (aux == null) {
                        mv.setViewName("Login/recuperar");
                        mv.addObject("msg", "Email não encontrado");
                        return mv;
                } else {
                        aux.setToken(Util.generateToken());
                        userRepository.save(aux);
                        String msg = "use esse token para recuperar sua senha:" + aux.getToken();
                        aux.setToken("");
                        mv.addObject("usuario", aux);
                        redirectAttributes.addFlashAttribute("usuario", aux);
                        serviceEmail.sendEmail("senaclpoo@gmail.com", aux.getEmail(), "Recuperação de Senha", msg);
                        mv.setViewName("redirect:/usuario/atualizarUsuario");
                }
                return mv;
        }
        @GetMapping("/usuario/atualizarUsuario")
        public ModelAndView updatePassword(Usuario user) {
                ModelAndView mv = new ModelAndView();
                mv.addObject("usuario", user);
                mv.setViewName("Login/atualizar");
                return mv;
        }

        @PostMapping("/usuario/atualizarUsuario")
        public ModelAndView updatePassPost(@ModelAttribute Usuario user, @RequestParam("confirmPassword") String confirmPassword) throws Exception {
                ModelAndView mv = new ModelAndView();
                Usuario aux = userRepository.findByEmail(user.getEmail());
                if (aux == null || !user.getToken().equals(aux.getToken())) {
                        mv.addObject("msg", "Token não encontrado!");
                        mv.addObject("usuario", user);
                        mv.setViewName("Login/atualizar");
                } else {
                        aux.setToken("");
                        aux.setPassword(passwordEncoder.encode(user.getPassword()));
                        userRepository.save(aux);
                        mv.addObject("usuario", new Usuario());
                        mv.setViewName("Login/login");
                }
                return mv;
        }

}