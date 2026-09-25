package nexoai.service;

import nexoai.dto.EventoRequest;
import nexoai.dto.PropuestaEvento;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PropuestaEventoService {

    private final Map<String, PropuestaEvento> propuestas =
            new ConcurrentHashMap<>();

    public PropuestaEvento crearPropuesta(
            String usuarioEmail,
            EventoRequest request) {

        String token = UUID.randomUUID().toString();

        PropuestaEvento propuesta = new PropuestaEvento(
                token,
                usuarioEmail,
                request,
                OffsetDateTime.now().plusMinutes(10)
        );

        propuestas.put(token, propuesta);

        return propuesta;
    }

    public PropuestaEvento obtenerPropuesta(
            String token,
            String usuarioEmail) {

        PropuestaEvento propuesta = propuestas.get(token);

        if (propuesta == null) {
            throw new IllegalArgumentException(
                    "La propuesta no existe."
            );
        }

        if (!propuesta.getUsuarioEmail().equals(usuarioEmail)) {
            throw new IllegalArgumentException(
                    "La propuesta pertenece a otro usuario."
            );
        }

        if (OffsetDateTime.now().isAfter(propuesta.getExpiraEn())) {
            propuestas.remove(token);

            throw new IllegalArgumentException(
                    "La propuesta ha expirado."
            );
        }

        return propuesta;
    }

    public void eliminarPropuesta(String token) {
        propuestas.remove(token);
    }
}