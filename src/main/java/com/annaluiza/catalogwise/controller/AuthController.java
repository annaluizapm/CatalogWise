package com.annaluiza.catalogwise.controller;

import com.annaluiza.catalogwise.dto.CadastroUsuarioDTO;
import com.annaluiza.catalogwise.dto.LoginDTO;
import com.annaluiza.catalogwise.dto.UsuarioRespostaDTO;
import com.annaluiza.catalogwise.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public UsuarioRespostaDTO cadastrar(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
        return usuarioService.cadastrar(cadastroUsuarioDTO);
    }

    @PostMapping("/login")
    public UsuarioRespostaDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return usuarioService.login(loginDTO);
    }
}