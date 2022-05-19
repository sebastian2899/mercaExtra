package com.mercaextra.app.domain;

import com.mercaextra.app.domain.enumeration.TipoFactura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion", precision = 21, scale = 2)
    private Instant fechaCreacion;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura")
    private TipoFactura tipoFactura;

    @Column(name = "informacion_proovedor")
    private String informacionProovedor;

    @Column(name = "id_proveedor")
    private Long idProveedor;

    @Column(name = "valor_factura", precision = 21, scale = 2)
    private BigDecimal valorFactura;

    @Column(name = "valor_pagado", precision = 21, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "valor_deuda", precision = 21, scale = 2)
    private BigDecimal valorDeuda;

    @Column(name = "estado")
    private String estado;

    @Transient
    private List<ItemFacturaCompra> itemsFacturaCompra;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public List<ItemFacturaCompra> getItemsFacturaCompra() {
        return itemsFacturaCompra;
    }

    public void setItemsFacturaCompra(List<ItemFacturaCompra> itemsFacturaCompra) {
        this.itemsFacturaCompra = itemsFacturaCompra;
    }

    public Compra id(Long id) {
        this.setId(id);
        return this;
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

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public Compra numeroFactura(String numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public TipoFactura getTipoFactura() {
        return this.tipoFactura;
    }

    public Compra tipoFactura(TipoFactura tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getInformacionProovedor() {
        return this.informacionProovedor;
    }

    public Compra informacionProovedor(String informacionProovedor) {
        this.setInformacionProovedor(informacionProovedor);
        return this;
    }

    public void setInformacionProovedor(String informacionProovedor) {
        this.informacionProovedor = informacionProovedor;
    }

    public Long getIdProveedor() {
        return this.idProveedor;
    }

    public Compra idProveedor(Long idProveedor) {
        this.setIdProveedor(idProveedor);
        return this;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public BigDecimal getValorFactura() {
        return this.valorFactura;
    }

    public Compra valorFactura(BigDecimal valorFactura) {
        this.setValorFactura(valorFactura);
        return this;
    }

    public void setValorFactura(BigDecimal valorFactura) {
        this.valorFactura = valorFactura;
    }

    public BigDecimal getValorPagado() {
        return this.valorPagado;
    }

    public Compra valorPagado(BigDecimal valorPagado) {
        this.setValorPagado(valorPagado);
        return this;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getValorDeuda() {
        return this.valorDeuda;
    }

    public Compra valorDeuda(BigDecimal valorDeuda) {
        this.setValorDeuda(valorDeuda);
        return this;
    }

    public void setValorDeuda(BigDecimal valorDeuda) {
        this.valorDeuda = valorDeuda;
    }

    public String getEstado() {
        return this.estado;
    }

    public Compra estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compra)) {
            return false;
        }
        return id != null && id.equals(((Compra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", fechaCreacion=" + getFechaCreacion() +
            ", numeroFactura='" + getNumeroFactura() + "'" +
            ", tipoFactura='" + getTipoFactura() + "'" +
            ", informacionProovedor='" + getInformacionProovedor() + "'" +
            ", idProveedor=" + getIdProveedor() +
            ", valorFactura=" + getValorFactura() +
            ", valorPagado=" + getValorPagado() +
            ", valorDeuda=" + getValorDeuda() +
            ", estado='" + getEstado() + "'" +
            "}";
    }

    public Compra fechaCreacion(Instant defaultFechaCreacion) {
        return this;
    }
}
