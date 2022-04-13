package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Pedido} entity.
 */
public class PedidoDTO implements Serializable {

    private Long id;

    private Instant fechaPedido;

    private String direccion;

    private String estado;

    private String infoDomicilio;

    private Long idDomiciliario;

    private Long idNotificacion;

    private Long idFactura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInfoDomicilio() {
        return infoDomicilio;
    }

    public void setInfoDomicilio(String infoDomicilio) {
        this.infoDomicilio = infoDomicilio;
    }

    public Long getIdDomiciliario() {
        return idDomiciliario;
    }

    public void setIdDomiciliario(Long idDomiciliario) {
        this.idDomiciliario = idDomiciliario;
    }

    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDTO)) {
            return false;
        }

        PedidoDTO pedidoDTO = (PedidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDTO{" +
            "id=" + getId() +
            ", fechaPedido='" + getFechaPedido() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", infoDomicilio='" + getInfoDomicilio() + "'" +
            ", idDomiciliario=" + getIdDomiciliario() +
            ", idNotificacion=" + getIdNotificacion() +
            ", idFactura=" + getIdFactura() +
            "}";
    }
}
