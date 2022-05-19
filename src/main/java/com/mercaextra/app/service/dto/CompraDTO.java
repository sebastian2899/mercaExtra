package com.mercaextra.app.service.dto;

import com.mercaextra.app.domain.enumeration.TipoFactura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Compra} entity.
 */
public class CompraDTO implements Serializable {

    private Long id;

    private Instant fechaCreacion;

    private String numeroFactura;

    private TipoFactura tipoFactura;

    private String informacionProovedor;

    private Long idProveedor;

    private BigDecimal valorFactura;

    private BigDecimal valorPagado;

    private BigDecimal valorDeuda;

    private String estado;

    private List<ItemFacturaCompraDTO> itemsFacturaCompra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemFacturaCompraDTO> getItemsFacturaCompra() {
        return itemsFacturaCompra;
    }

    public void setItemsFacturaCompra(List<ItemFacturaCompraDTO> itemsFacturaCompra) {
        this.itemsFacturaCompra = itemsFacturaCompra;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public String getInformacionProovedor() {
        return informacionProovedor;
    }

    public void setInformacionProovedor(String informacionProovedor) {
        this.informacionProovedor = informacionProovedor;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
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
        if (!(o instanceof CompraDTO)) {
            return false;
        }

        CompraDTO compraDTO = (CompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraDTO{" +
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
}
