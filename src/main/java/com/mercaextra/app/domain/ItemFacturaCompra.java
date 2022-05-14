package com.mercaextra.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "item_factura_compra")
public class ItemFacturaCompra implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "precio", precision = 21, scale = 2)
    private BigDecimal precio;

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
        if (!(obj instanceof ItemFacturaCompra)) return false;
        ItemFacturaCompra other = (ItemFacturaCompra) obj;
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
            "ItemFacturaCompra [id=" +
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
