package br.git.francieudocosta.dev.usuario.infrastructure.repository;

import br.git.francieudocosta.dev.usuario.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
