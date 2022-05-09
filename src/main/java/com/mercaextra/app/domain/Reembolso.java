package com.mercaextra.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reembolso.
 */
@Entity
@Table(name = "reembolso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reembolso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "id_domiciliario")
    private Long idDomiciliario;

    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_reembolso")
    private Instant fechaReembolso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Instant getFechaReembolso() {
        return fechaReembolso;
    }

    public void setFechaReembolso(Instant fechaReembolso) {
        this.fechaReembolso = fechaReembolso;
    }

    public Reembolso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPedido() {
        return this.idPedido;
    }

    public Reembolso idPedido(Long idPedido) {
        this.setIdPedido(idPedido);
        return this;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdDomiciliario() {
        return this.idDomiciliario;
    }

    public Reembolso idDomiciliario(Long idDomiciliario) {
        this.setIdDomiciliario(idDomiciliario);
        return this;
    }

    public void setIdDomiciliario(Long idDomiciliario) {
        this.idDomiciliario = idDomiciliario;
    }

    public Long getIdFactura() {
        return this.idFactura;
    }

    public Reembolso idFactura(Long idFactura) {
        this.setIdFactura(idFactura);
        return this;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Reembolso descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return this.estado;
    }

    public Reembolso estado(String estado) {
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
        if (!(o instanceof Reembolso)) {
            return false;
        }
        return id != null && id.equals(((Reembolso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reembolso{" +
            "id=" + getId() +
            ", idPedido=" + getIdPedido() +
            ", idDomiciliario=" + getIdDomiciliario() +
            ", idFactura=" + getIdFactura() +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
