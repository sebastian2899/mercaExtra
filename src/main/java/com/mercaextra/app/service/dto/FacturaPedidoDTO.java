package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class FacturaPedidoDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long idFactura;

    private String infoCliente;

    private String numeroFactura;

    private BigDecimal valorFactura;

    private String estadoFactura;

    public String getInfoCliente() {
        return infoCliente;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    @Override
    public int hashCode() {
        return Objects.hash(estadoFactura, infoCliente, numeroFactura, valorFactura);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FacturaPedidoDTO)) return false;
        FacturaPedidoDTO other = (FacturaPedidoDTO) obj;
        return (
            Objects.equals(estadoFactura, other.estadoFactura) &&
            Objects.equals(infoCliente, other.infoCliente) &&
            Objects.equals(numeroFactura, other.numeroFactura) &&
            Objects.equals(valorFactura, other.valorFactura)
        );
    }
}
