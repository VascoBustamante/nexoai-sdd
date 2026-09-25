package nexoai.service;

import nexoai.dto.EventoRequest;
import nexoai.model.Evento;
import nexoai.model.PerfilUsuario;
import nexoai.repository.EventoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventoServiceTest {

    private EventoRepository eventoRepository;
    private EventoService eventoService;

    @BeforeEach
    void setUp() {

        eventoRepository =
                mock(EventoRepository.class);

        eventoService =
                new EventoService(eventoRepository);
    }

    // =====================================================
    // TEST 1
    // REG-03: intervalo válido
    // =====================================================

    @Test
    void debeAceptarIntervaloValido() {

        EventoRequest request =
                crearRequest(
                        "Clase de Diseño de Software",
                        "2026-09-25T10:00:00-05:00",
                        "2026-09-25T11:00:00-05:00"
                );

        assertDoesNotThrow(
                () -> eventoService.validarIntervalo(request)
        );
    }

    // =====================================================
    // TEST 2
    // REG-03: fin anterior al inicio
    // =====================================================

    @Test
    void debeRechazarFinAnteriorAlInicio() {

        EventoRequest request =
                crearRequest(
                        "Evento inválido",
                        "2026-09-25T15:00:00-05:00",
                        "2026-09-25T14:00:00-05:00"
                );

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> eventoService.validarIntervalo(request)
                );

        assertEquals(
                "La fecha y hora de finalización deben ser posteriores al inicio.",
                excepcion.getMessage()
        );
    }

    // =====================================================
    // TEST 3
    // REG-03: inicio y fin iguales
    // =====================================================

    @Test
    void debeRechazarInicioYFinIguales() {

        EventoRequest request =
                crearRequest(
                        "Evento inválido",
                        "2026-09-25T15:00:00-05:00",
                        "2026-09-25T15:00:00-05:00"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> eventoService.validarIntervalo(request)
        );
    }

    // =====================================================
    // TEST 4
    // REG-04: detectar superposición
    // =====================================================

    @Test
    void debeDetectarSuperposicion() {

        PerfilUsuario perfil =
                mock(PerfilUsuario.class);

        when(perfil.getId())
                .thenReturn(1L);

        EventoRequest request =
                crearRequest(
                        "Reunión proyecto NexoAI",
                        "2026-09-25T10:30:00-05:00",
                        "2026-09-25T11:30:00-05:00"
                );

        Evento eventoExistente =
                new Evento();

        eventoExistente.setTitulo(
                "Clase de Diseño de Software"
        );

        when(
                eventoRepository.buscarSuperposiciones(
                        eq(1L),
                        eq(request.getInicio()),
                        eq(request.getFin())
                )
        ).thenReturn(
                List.of(eventoExistente)
        );

        List<Evento> conflictos =
                eventoService.buscarSuperposiciones(
                        perfil,
                        request
                );

        assertFalse(
                conflictos.isEmpty()
        );

        assertEquals(
                1,
                conflictos.size()
        );

        assertEquals(
                "Clase de Diseño de Software",
                conflictos.get(0).getTitulo()
        );
    }

    // =====================================================
    // TEST 5
    // REG-04: evento sin superposición
    // =====================================================

    @Test
    void noDebeDetectarSuperposicionCuandoNoExisteConflicto() {

        PerfilUsuario perfil =
                mock(PerfilUsuario.class);

        when(perfil.getId())
                .thenReturn(1L);

        EventoRequest request =
                crearRequest(
                        "Estudiar Diseño de Software",
                        "2026-09-25T16:00:00-05:00",
                        "2026-09-25T17:00:00-05:00"
                );

        when(
                eventoRepository.buscarSuperposiciones(
                        eq(1L),
                        eq(request.getInicio()),
                        eq(request.getFin())
                )
        ).thenReturn(
                Collections.emptyList()
        );

        List<Evento> conflictos =
                eventoService.buscarSuperposiciones(
                        perfil,
                        request
                );

        assertTrue(
                conflictos.isEmpty()
        );
    }

    // =====================================================
    // TEST 6
    // REG-05: registrar evento
    // =====================================================

    @Test
    void debeRegistrarEventoConfirmado() {

        PerfilUsuario perfil =
                mock(PerfilUsuario.class);

        EventoRequest request =
                crearRequest(
                        "Clase de Diseño de Software",
                        "2026-09-25T10:00:00-05:00",
                        "2026-09-25T11:00:00-05:00"
                );

        when(
                eventoRepository.save(any(Evento.class))
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        Evento resultado =
                eventoService.registrarEvento(
                        perfil,
                        request
                );

        assertNotNull(resultado);

        assertEquals(
                "Clase de Diseño de Software",
                resultado.getTitulo()
        );

        assertEquals(
                perfil,
                resultado.getPerfilUsuario()
        );

        verify(
                eventoRepository,
                times(1)
        ).save(any(Evento.class));
    }

    // =====================================================
    // MÉTODO AUXILIAR
    // =====================================================

    private EventoRequest crearRequest(
            String titulo,
            String inicio,
            String fin) {

        EventoRequest request =
                new EventoRequest();

        request.setTitulo(titulo);

        request.setDescripcion(
                "Prueba automática SDD"
        );

        request.setInicio(
                OffsetDateTime.parse(inicio)
        );

        request.setFin(
                OffsetDateTime.parse(fin)
        );

        return request;
    }
}