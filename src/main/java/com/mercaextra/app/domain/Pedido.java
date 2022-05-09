package com.mercaextra.app.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_pedido")
    private Instant fechaPedido;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "info_domicilio")
    private String infoDomicilio;

    @Column(name = "id_domiciliario")
    private Long idDomiciliario;

    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "user_name")
    private String userName;

    @Transient
    private String descripcionNotificacion;

    @Column(name = "hora_despacho")
    private String horaDespacho;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getDescripcionNotificacion() {
        return descripcionNotificacion;
    }

    public void setDescripcionNotificacion(String descripcionNotificacion) {
        this.descripcionNotificacion = descripcionNotificacion;
    }

    public Long getId() {
        return this.id;
    }

    public String getHoraDespacho() {
        return horaDespacho;
    }

    public void setHoraDespacho(String horaDespacho) {
        this.horaDespacho = horaDespacho;
    }

    public Pedido id(Long id) {
        this.setId(id);
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaPedido() {
        return this.fechaPedido;
    }

    public Pedido fechaPedido(Instant fechaPedido) {
        this.setFechaPedido(fechaPedido);
        return this;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Pedido direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return this.estado;
    }

    public Pedido estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInfoDomicilio() {
        return this.infoDomicilio;
    }

    public Pedido infoDomicilio(String infoDomicilio) {
        this.setInfoDomicilio(infoDomicilio);
        return this;
    }

    public void setInfoDomicilio(String infoDomicilio) {
        this.infoDomicilio = infoDomicilio;
    }

    public Long getIdDomiciliario() {
        return this.idDomiciliario;
    }

    public Pedido idDomiciliario(Long idDomiciliario) {
        this.setIdDomiciliario(idDomiciliario);
        return this;
    }

    public void setIdDomiciliario(Long idDomiciliario) {
        this.idDomiciliario = idDomiciliario;
    }

    public Long getIdNotificacion() {
        return this.idNotificacion;
    }

    public Pedido idNotificacion(Long idNotificacion) {
        this.setIdNotificacion(idNotificacion);
        return this;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Long getIdFactura() {
        return this.idFactura;
    }

    public Pedido idFactura(Long idFactura) {
        this.setIdFactura(idFactura);
        return this;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return id != null && id.equals(((Pedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
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
