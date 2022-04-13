package com.mercaextra.app.service.dto;

import com.mercaextra.app.domain.enumeration.MetodoPago;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Factura} entity.
 */
public class FacturaDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private String infoCiente;

    private String numeroFactura;

    private TipoFactura tipoFactura;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private BigDecimal estadoFactura;

    private MetodoPago metodoPago;

    private String userName;

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

    public String getInfoCiente() {
        return infoCiente;
    }

    public void setInfoCiente(String infoCiente) {
        this.infoCiente = infoCiente;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return valorDeuda;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public BigDecimal getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(BigDecimal estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaDTO)) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", infoCiente='" + getInfoCiente() + "'" +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", estadoFactura=" + getEstadoFactura() +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", userName='" + getUserName() + "'" +
            "}";
    }
}
