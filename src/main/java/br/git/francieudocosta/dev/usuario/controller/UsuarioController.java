package br.git.francieudocosta.dev.usuario.controller;

import br.git.francieudocosta.dev.usuario.business.UsuarioService;
import br.git.francieudocosta.dev.usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(UsuarioDTO usuarioDTO) {

        return ResponseEntity.ok(usuarioService.salvarUsuario(usuarioDTO));
    }
}
