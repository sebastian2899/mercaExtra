package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Reembolso} entity.
 */
public class ReembolsoDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long idPedido;

    private Long idDomiciliario;

    private Long idFactura;

    private String descripcion;

    private String estado;

    private Instant fechaReembolso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaReembolso() {
        return fechaReembolso;
    }

    public void setFechaReembolso(Instant fechaReembolso) {
        this.fechaReembolso = fechaReembolso;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdDomiciliario() {
        return idDomiciliario;
    }

    public void setIdDomiciliario(Long idDomiciliario) {
        this.idDomiciliario = idDomiciliario;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReembolsoDTO)) {
            return false;
        }

        ReembolsoDTO reembolsoDTO = (ReembolsoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reembolsoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReembolsoDTO{" +
            "id=" + getId() +
            ", idPedido=" + getIdPedido() +
            ", idDomiciliario=" + getIdDomiciliario() +
            ", idFactura=" + getIdFactura() +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
