package nexoai.service;

import nexoai.dto.EventoRequest;
import nexoai.dto.PropuestaEvento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PropuestaEventoServiceTest {

    private PropuestaEventoService propuestaEventoService;

    @BeforeEach
    void setUp() {
        propuestaEventoService =
                new PropuestaEventoService();
    }

    // =====================================================
    // TEST 1
    // Crear una propuesta genera un token
    // =====================================================

    @Test
    void debeCrearPropuestaConToken() {

        EventoRequest request =
                crearRequest();

        PropuestaEvento propuesta =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request
                );

        assertNotNull(propuesta);

        assertNotNull(
                propuesta.getToken()
        );

        assertFalse(
                propuesta.getToken().isBlank()
        );

        assertEquals(
                "demo@nexoai.com",
                propuesta.getUsuarioEmail()
        );

        assertEquals(
                request,
                propuesta.getEvento()
        );
    }

    // =====================================================
    // TEST 2
    // El propietario puede recuperar su propuesta
    // =====================================================

    @Test
    void propietarioDebePoderObtenerPropuesta() {

        EventoRequest request =
                crearRequest();

        PropuestaEvento creada =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request
                );

        PropuestaEvento obtenida =
                propuestaEventoService.obtenerPropuesta(
                        creada.getToken(),
                        "demo@nexoai.com"
                );

        assertNotNull(obtenida);

        assertEquals(
                creada.getToken(),
                obtenida.getToken()
        );

        assertEquals(
                "demo@nexoai.com",
                obtenida.getUsuarioEmail()
        );
    }

    // =====================================================
    // TEST 3
    // Otro usuario NO puede utilizar la propuesta
    // =====================================================

    @Test
    void debeRechazarPropuestaDeOtroUsuario() {

        EventoRequest request =
                crearRequest();

        PropuestaEvento propuesta =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request
                );

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                propuestaEventoService
                                        .obtenerPropuesta(
                                                propuesta.getToken(),
                                                "otro@nexoai.com"
                                        )
                );

        assertEquals(
                "La propuesta pertenece a otro usuario.",
                excepcion.getMessage()
        );
    }

    // =====================================================
    // TEST 4
    // Un token inexistente debe rechazarse
    // =====================================================

    @Test
    void debeRechazarTokenInexistente() {

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                propuestaEventoService
                                        .obtenerPropuesta(
                                                "token-que-no-existe",
                                                "demo@nexoai.com"
                                        )
                );

        assertEquals(
                "La propuesta no existe.",
                excepcion.getMessage()
        );
    }

    // =====================================================
    // TEST 5
    // Cancelar elimina la propuesta
    // =====================================================

    @Test
    void debeEliminarPropuestaAlCancelar() {

        EventoRequest request =
                crearRequest();

        PropuestaEvento propuesta =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request
                );

        propuestaEventoService.eliminarPropuesta(
                propuesta.getToken()
        );

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                propuestaEventoService
                                        .obtenerPropuesta(
                                                propuesta.getToken(),
                                                "demo@nexoai.com"
                                        )
                );

        assertEquals(
                "La propuesta no existe.",
                excepcion.getMessage()
        );
    }

    // =====================================================
    // TEST 6
    // Dos propuestas deben tener tokens diferentes
    // =====================================================

    @Test
    void propuestasDiferentesDebenTenerTokensDiferentes() {

        EventoRequest request1 =
                crearRequest();

        EventoRequest request2 =
                crearRequest();

        PropuestaEvento propuesta1 =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request1
                );

        PropuestaEvento propuesta2 =
                propuestaEventoService.crearPropuesta(
                        "demo@nexoai.com",
                        request2
                );

        assertNotEquals(
                propuesta1.getToken(),
                propuesta2.getToken()
        );
    }

    // =====================================================
    // MÉTODO AUXILIAR
    // =====================================================

    private EventoRequest crearRequest() {

        EventoRequest request =
                new EventoRequest();

        request.setTitulo(
                "Clase de Diseño de Software"
        );

        request.setDescripcion(
                "Prueba automática SDD"
        );

        request.setInicio(
                OffsetDateTime.parse(
                        "2026-09-25T10:00:00-05:00"
                )
        );

        request.setFin(
                OffsetDateTime.parse(
                        "2026-09-25T11:00:00-05:00"
                )
        );

        return request;
    }
}