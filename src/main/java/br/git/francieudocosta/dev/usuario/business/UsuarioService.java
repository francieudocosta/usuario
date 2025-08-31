package br.git.francieudocosta.dev.usuario.business;

import br.git.francieudocosta.dev.usuario.business.converter.UsuarioConverter;
import br.git.francieudocosta.dev.usuario.business.dto.EnderecoDTO;
import br.git.francieudocosta.dev.usuario.business.dto.TelefoneDTO;
import br.git.francieudocosta.dev.usuario.business.dto.UsuarioDTO;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Endereco;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Telefone;
import br.git.francieudocosta.dev.usuario.infrastructure.entity.Usuario;
import br.git.francieudocosta.dev.usuario.infrastructure.exceptions.ConflictException;
import br.git.francieudocosta.dev.usuario.infrastructure.exceptions.ResourceNotFoundException;
import br.git.francieudocosta.dev.usuario.infrastructure.repository.EnderecoRepository;
import br.git.francieudocosta.dev.usuario.infrastructure.repository.TelefoneRepository;
import br.git.francieudocosta.dev.usuario.infrastructure.repository.UsuarioRepository;
import br.git.francieudocosta.dev.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {

        try{
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(() ->
                    new ResourceNotFoundException("Usuario não encontrado " + email)));

        }catch (ResourceNotFoundException e){

            throw new ResourceNotFoundException("Email não acontrado " + email);
        }

    }

    public void deletarUsuario(String email) {

        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizarDadosUsuario( UsuarioDTO usuarioDTO, String token) {

        String email = jwtUtil.extractUsername(token.substring(7));

        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado " + email));

        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizarEnderecoUsuario(Long idEndereco, EnderecoDTO enderecoDTO) {

        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("id não encontrado " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizarTelefoneUsuario(Long idTelefone, TelefoneDTO telefoneDTO) {

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("id não encontrado " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastroEndereco(EnderecoDTO enderecoDTO, String token){

        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado " + email));

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(
                usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId())
        ));
    }

    public TelefoneDTO cadastroTelefone(TelefoneDTO telefoneDTO, String token){

        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado " + email));

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(
                usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId())));
    }
}
