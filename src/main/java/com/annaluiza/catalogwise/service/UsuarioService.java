package com.annaluiza.catalogwise.service;

import com.annaluiza.catalogwise.dto.CadastroUsuarioDTO;
import com.annaluiza.catalogwise.dto.LoginDTO;
import com.annaluiza.catalogwise.dto.UsuarioRespostaDTO;
import com.annaluiza.catalogwise.model.Usuario;
import com.annaluiza.catalogwise.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioRespostaDTO cadastrar(CadastroUsuarioDTO cadastroUsuarioDTO) {
        boolean emailJaExiste = usuarioRepository.findByEmail(cadastroUsuarioDTO.getEmail()).isPresent();

        if (emailJaExiste) {
            throw new RuntimeException("Já existe um usuário cadastrado com este email");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(cadastroUsuarioDTO.getNome());
        usuario.setEmail(cadastroUsuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(cadastroUsuarioDTO.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioRespostaDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }

    public UsuarioRespostaDTO login(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        boolean senhaCorreta = passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha());

        if (!senhaCorreta) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        return new UsuarioRespostaDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}