package nexoai.dto;

import java.time.OffsetDateTime;

public class PropuestaEvento {

    private final String token;
    private final String usuarioEmail;
    private final EventoRequest evento;
    private final OffsetDateTime expiraEn;

    public PropuestaEvento(
            String token,
            String usuarioEmail,
            EventoRequest evento,
            OffsetDateTime expiraEn) {

        this.token = token;
        this.usuarioEmail = usuarioEmail;
        this.evento = evento;
        this.expiraEn = expiraEn;
    }

    public String getToken() {
        return token;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public EventoRequest getEvento() {
        return evento;
    }

    public OffsetDateTime getExpiraEn() {
        return expiraEn;
    }
}