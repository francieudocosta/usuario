package br.git.francieudocosta.dev.usuario.business;

import br.git.francieudocosta.dev.usuario.business.converter.UsuarioConverter;
import br.git.francieudocosta.dev.usuario.business.dto.UsuarioDTO;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Usuario;
import br.git.francieudocosta.dev.usuario.infrastructure.exceptions.ConflictException;
import br.git.francieudocosta.dev.usuario.infrastructure.exceptions.ResourceNotFoundException;
import br.git.francieudocosta.dev.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {

        emailExistente(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExistente(String email) {

        try{
            boolean existe = verificarEmailExistente(email);

            if(existe) {
                throw  new ConflictException("Email já cadastrado " + email);
            }

        }catch(ConflictException e){

            throw new ConflictException("Email já cadastrado " +e.getCause());
        }
    }

    public boolean verificarEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {

        return usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Usuario não encontrado " + email));
    }

    public void deletarUsuario(String email) {

        usuarioRepository.deleteByEmail(email);
    }
}
