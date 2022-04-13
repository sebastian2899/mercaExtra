package com.mercaextra.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Egreso.
 */
@Entity
@Table(name = "egreso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Egreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "valor_egreso", precision = 21, scale = 2)
    private BigDecimal valorEgreso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Egreso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Egreso fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Egreso descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorEgreso() {
        return this.valorEgreso;
    }

    public Egreso valorEgreso(BigDecimal valorEgreso) {
        this.setValorEgreso(valorEgreso);
        return this;
    }

    public void setValorEgreso(BigDecimal valorEgreso) {
        this.valorEgreso = valorEgreso;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Egreso)) {
            return false;
        }
        return id != null && id.equals(((Egreso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Egreso{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", valorEgreso=" + getValorEgreso() +
            "}";
    }
}
