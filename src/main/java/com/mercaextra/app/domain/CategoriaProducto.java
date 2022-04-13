package com.mercaextra.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CategoriaProducto.
 */
@Entity
@Table(name = "categoria_producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategoriaProducto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoriaProducto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return this.nombreCategoria;
    }

    public CategoriaProducto nombreCategoria(String nombreCategoria) {
        this.setNombreCategoria(nombreCategoria);
        return this;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public CategoriaProducto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaProducto)) {
            return false;
        }
        return id != null && id.equals(((CategoriaProducto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaProducto{" +
            "id=" + getId() +
            ", nombreCategoria='" + getNombreCategoria() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
