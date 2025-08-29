package br.git.francieudocosta.dev.usuario.business;

import br.git.francieudocosta.dev.usuario.business.converter.UsuarioConverter;
import br.git.francieudocosta.dev.usuario.business.dto.UsuarioDTO;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Usuario;
import br.git.francieudocosta.dev.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
