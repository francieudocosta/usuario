package br.git.francieudocosta.dev.usuario.infrastructure.repository;

import br.git.francieudocosta.dev.aprendendospring.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
