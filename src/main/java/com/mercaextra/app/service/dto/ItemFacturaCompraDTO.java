package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ItemFacturaCompraDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long idFactura;

    private Long idProducto;

    private Long cantidad;

    private BigDecimal precio;

    private String nombreProducto;

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, id, idFactura, idProducto, precio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemFacturaCompraDTO)) return false;
        ItemFacturaCompraDTO other = (ItemFacturaCompraDTO) obj;
        return (
            Objects.equals(cantidad, other.cantidad) &&
            Objects.equals(id, other.id) &&
            Objects.equals(idFactura, other.idFactura) &&
            Objects.equals(idProducto, other.idProducto) &&
            Objects.equals(precio, other.precio)
        );
    }

    @Override
    public String toString() {
        return (
            "ItemFacturaCompraDTO [id=" +
            id +
            ", idFactura=" +
            idFactura +
            ", idProducto=" +
            idProducto +
            ", cantidad=" +
            cantidad +
            ", precio=" +
            precio +
            "]"
        );
    }
}
