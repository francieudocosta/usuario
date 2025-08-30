package br.git.francieudocosta.dev.usuario.controller;

import br.git.francieudocosta.dev.usuario.business.UsuarioService;
import br.git.francieudocosta.dev.usuario.business.dto.UsuarioDTO;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Usuario;
import br.git.francieudocosta.dev.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO) {

        return ResponseEntity.ok(usuarioService.salvarUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
        );

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@RequestParam("email") String email) {

        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String email) {

        usuarioService.deletarUsuario(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(usuarioDTO, token));
    }
}
