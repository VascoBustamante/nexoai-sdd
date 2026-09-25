package nexoai.repository;

import nexoai.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventoRepository
        extends JpaRepository<Evento, Long> {

    List<Evento> findByPerfilUsuarioIdOrderByInicioAsc(Long perfilUsuarioId);

    @Query("""
        SELECT e
        FROM Evento e
        WHERE e.perfilUsuario.id = :perfilId
          AND e.inicio < :fin
          AND e.fin > :inicio
    """)
    List<Evento> buscarSuperposiciones(
            @Param("perfilId") Long perfilId,
            @Param("inicio") OffsetDateTime inicio,
            @Param("fin") OffsetDateTime fin
    );
}