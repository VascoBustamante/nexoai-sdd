package nexoai.controller;

import jakarta.validation.Valid;

import nexoai.dto.EventoRequest;
import nexoai.dto.PropuestaEvento;
import nexoai.model.Evento;
import nexoai.model.PerfilUsuario;
import nexoai.repository.PerfilUsuarioRepository;
import nexoai.service.EventoService;
import nexoai.service.PropuestaEventoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final PropuestaEventoService propuestaEventoService;
    private final PerfilUsuarioRepository perfilUsuarioRepository;

    public EventoController(
            EventoService eventoService,
            PropuestaEventoService propuestaEventoService,
            PerfilUsuarioRepository perfilUsuarioRepository) {

        this.eventoService = eventoService;
        this.propuestaEventoService = propuestaEventoService;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
    }

    // =========================================================
    // EVALUAR EVENTO
    // =========================================================

    @PostMapping("/evaluar")
    public ResponseEntity<?> evaluar(
            @Valid @RequestBody EventoRequest request,
            BindingResult bindingResult,
            Authentication authentication) {

        // REG-02: validar campos obligatorios
        if (bindingResult.hasErrors()) {

            String mensaje = bindingResult
                    .getFieldErrors()
                    .get(0)
                    .getDefaultMessage();

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "estado", "ERROR_VALIDACION",
                            "mensaje", mensaje
                    ));
        }

        try {

            // REG-01: obtener al usuario autenticado
            PerfilUsuario perfil =
                    obtenerPerfil(authentication);

            // REG-03: fin debe ser posterior al inicio
            eventoService.validarIntervalo(request);

            // REG-04: buscar eventos superpuestos
            List<Evento> conflictos =
                    eventoService.buscarSuperposiciones(
                            perfil,
                            request
                    );

            // Se crea una propuesta temporal.
            // Todavía NO se registra el evento.
            PropuestaEvento propuesta =
                    propuestaEventoService.crearPropuesta(
                            perfil.getEmail(),
                            request
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "estado", "REQUIERE_CONFIRMACION",
                            "token", propuesta.getToken(),
                            "hayConflicto", !conflictos.isEmpty(),
                            "conflictos", conflictos.stream()
                                    .map(Evento::getTitulo)
                                    .toList()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "estado", "ERROR_VALIDACION",
                            "mensaje", e.getMessage()
                    ));
        }
    }

    // =========================================================
    // CONFIRMAR EVENTO
    // =========================================================

    @PostMapping("/confirmar/{token}")
    public ResponseEntity<?> confirmar(
            @PathVariable String token,
            Authentication authentication) {

        try {

            PerfilUsuario perfil =
                    obtenerPerfil(authentication);

            // Verificar que la propuesta exista,
            // pertenezca al usuario y no haya expirado.
            PropuestaEvento propuesta =
                    propuestaEventoService.obtenerPropuesta(
                            token,
                            perfil.getEmail()
                    );

            // REG-05:
            // registrar definitivamente el evento
            // asociado al perfil autenticado.
            Evento evento =
                    eventoService.registrarEvento(
                            perfil,
                            propuesta.getEvento()
                    );

            // El token ya fue utilizado.
            propuestaEventoService.eliminarPropuesta(token);

            return ResponseEntity.ok(
                    Map.of(
                            "estado", "REGISTRADO",
                            "eventoId", evento.getId(),
                            "mensaje",
                            "El evento fue registrado correctamente."
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "estado", "ERROR",
                            "mensaje", e.getMessage()
                    ));
        }
    }

    // =========================================================
    // CANCELAR PROPUESTA
    // =========================================================

    @PostMapping("/cancelar/{token}")
    public ResponseEntity<?> cancelar(
            @PathVariable String token,
            Authentication authentication) {

        try {

            PerfilUsuario perfil =
                    obtenerPerfil(authentication);

            // Comprobar que el token pertenece
            // realmente al usuario autenticado.
            propuestaEventoService.obtenerPropuesta(
                    token,
                    perfil.getEmail()
            );

            // Eliminar la propuesta.
            // El evento NO se guarda en PostgreSQL.
            propuestaEventoService.eliminarPropuesta(token);

            return ResponseEntity.ok(
                    Map.of(
                            "estado", "CANCELADO",
                            "mensaje",
                            "Registro cancelado. El evento no fue creado."
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "estado", "ERROR",
                            "mensaje", e.getMessage()
                    ));
        }
    }

    // =========================================================
    // LISTAR EVENTOS DEL USUARIO
    // =========================================================

    @GetMapping
    public List<Evento> listar(
            Authentication authentication) {

        PerfilUsuario perfil =
                obtenerPerfil(authentication);

        return eventoService.obtenerEventos(perfil);
    }

    // =========================================================
    // OBTENER PERFIL AUTENTICADO
    // =========================================================

    private PerfilUsuario obtenerPerfil(
            Authentication authentication) {

        return perfilUsuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Perfil de usuario no encontrado."
                        )
                );
    }
}