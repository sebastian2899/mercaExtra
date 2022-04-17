package com.mercaextra.app.domain;

import com.mercaextra.app.domain.enumeration.MetodoPago;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "info_ciente")
    private String infoCiente;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura")
    private TipoFactura tipoFactura;

    @Column(name = "valor_factura", precision = 21, scale = 2)
    private BigDecimal valorFactura;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "valor_deuda", precision = 21, scale = 2)
    private BigDecimal valorDeuda;

    @Column(name = "estado_factura")
    private String estadoFactura;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Column(name = "user_name")
    private String userName;

    @Transient
    private List<ItemFacturaVenta> itemsPorFactura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public List<ItemFacturaVenta> getItemsPorFactura() {
        return itemsPorFactura;
    }

    public void setItemsPorFactura(List<ItemFacturaVenta> itemsPorFactura) {
        this.itemsPorFactura = itemsPorFactura;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Factura fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getInfoCiente() {
        return this.infoCiente;
    }

    public Factura infoCiente(String infoCiente) {
        this.setInfoCiente(infoCiente);
        return this;
    }

    public void setInfoCiente(String infoCiente) {
        this.infoCiente = infoCiente;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public Factura numeroFactura(String numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public TipoFactura getTipoFactura() {
        return this.tipoFactura;
    }

    public Factura tipoFactura(TipoFactura tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public BigDecimal getValorFactura() {
        return this.valorFactura;
    }

    public Factura valorFactura(BigDecimal valorFactura) {
        this.setValorFactura(valorFactura);
        return this;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public Factura valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public Factura valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public MetodoPago getMetodoPago() {
        return this.metodoPago;
    }

    public Factura metodoPago(MetodoPago metodoPago) {
        this.setMetodoPago(metodoPago);
        return this;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getUserName() {
        return this.userName;
    }

    public Factura userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
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

    public Factura estadoFactura(String defaultEstadoFactura) {
        // TODO Auto-generated method stub
        return null;
    }
}
