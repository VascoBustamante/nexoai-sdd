package nexoai.dto;

import java.time.OffsetDateTime;

public class EventoResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private OffsetDateTime inicio;
    private OffsetDateTime fin;

    public EventoResponse(
            Long id,
            String titulo,
            String descripcion,
            OffsetDateTime inicio,
            OffsetDateTime fin) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public OffsetDateTime getInicio() {
        return inicio;
    }

    public OffsetDateTime getFin() {
        return fin;
    }
}