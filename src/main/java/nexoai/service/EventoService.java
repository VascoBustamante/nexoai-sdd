package nexoai.service;

import nexoai.dto.EventoRequest;
import nexoai.model.Evento;
import nexoai.model.PerfilUsuario;
import nexoai.repository.EventoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    // REG-03: el fin debe ser posterior al inicio
    public void validarIntervalo(EventoRequest request) {

        if (request.getInicio() == null || request.getFin() == null) {
            return;
        }

        if (!request.getFin().isAfter(request.getInicio())) {
            throw new IllegalArgumentException(
                    "La fecha y hora de finalización deben ser posteriores al inicio."
            );
        }
    }

    // REG-04: detectar superposiciones
    public List<Evento> buscarSuperposiciones(
            PerfilUsuario perfil,
            EventoRequest request) {

        validarIntervalo(request);

        return eventoRepository.buscarSuperposiciones(
                perfil.getId(),
                request.getInicio(),
                request.getFin()
        );
    }

    // REG-05: registrar el evento asociado al usuario
    public Evento registrarEvento(
            PerfilUsuario perfil,
            EventoRequest request) {

        validarIntervalo(request);

        Evento evento = new Evento(
                request.getTitulo(),
                request.getDescripcion(),
                request.getInicio(),
                request.getFin(),
                perfil
        );

        return eventoRepository.save(evento);
    }

    public List<Evento> obtenerEventos(PerfilUsuario perfil) {

        return eventoRepository
                .findByPerfilUsuarioIdOrderByInicioAsc(
                        perfil.getId()
                );
    }
}