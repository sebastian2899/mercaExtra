package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DatosPedidoReembolsoDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String fechaPedido;

    private BigDecimal valorFactura;

    private String pedidoDireccion;

    private String domiciliario;

    private Long idPedido;

    private Long idDomiciliario;

    private Long idFactura;

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public String getDomiciliario() {
        return domiciliario;
    }

    public void setDomiciliario(String domiciliario) {
        this.domiciliario = domiciliario;
    }

    public String getPedidoDireccion() {
        return pedidoDireccion;
    }

    public void setPedidoDireccion(String pedidoDireccion) {
        this.pedidoDireccion = pedidoDireccion;
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
}
