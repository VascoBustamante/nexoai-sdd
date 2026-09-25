package nexoai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class EventoRequest {

    @NotBlank(message = "El título es obligatorio.")
    private String titulo;

    private String descripcion;

    @NotNull(message = "La fecha y hora de inicio son obligatorias.")
    private OffsetDateTime inicio;

    @NotNull(message = "La fecha y hora de finalización son obligatorias.")
    private OffsetDateTime fin;

    public EventoRequest() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OffsetDateTime getInicio() {
        return inicio;
    }

    public void setInicio(OffsetDateTime inicio) {
        this.inicio = inicio;
    }

    public OffsetDateTime getFin() {
        return fin;
    }

    public void setFin(OffsetDateTime fin) {
        this.fin = fin;
    }
}