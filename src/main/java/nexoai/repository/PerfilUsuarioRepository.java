package nexoai.repository;

import nexoai.model.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilUsuarioRepository
        extends JpaRepository<PerfilUsuario, Long> {

    Optional<PerfilUsuario> findByEmail(String email);
}