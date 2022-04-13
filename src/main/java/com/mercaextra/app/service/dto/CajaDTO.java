package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Caja} entity.
 */
public class CajaDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private BigDecimal valorTotalDia;

    private BigDecimal valorRegistradoDia;

    private BigDecimal diferencia;

    private String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getValorTotalDia() {
        return valorTotalDia;
    }

    public void setValorTotalDia(BigDecimal valorTotalDia) {
        this.valorTotalDia = valorTotalDia;
    }

    public BigDecimal getValorRegistradoDia() {
        return valorRegistradoDia;
    }

    public void setValorRegistradoDia(BigDecimal valorRegistradoDia) {
        this.valorRegistradoDia = valorRegistradoDia;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
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
        if (!(o instanceof CajaDTO)) {
            return false;
        }

        CajaDTO cajaDTO = (CajaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", valorTotalDia=" + getValorTotalDia() +
            ", valorRegistradoDia=" + getValorRegistradoDia() +
            ", diferencia=" + getDiferencia() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
