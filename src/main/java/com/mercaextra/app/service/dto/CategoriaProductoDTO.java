package com.mercaextra.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.CategoriaProducto} entity.
 */
public class CategoriaProductoDTO implements Serializable {

    private Long id;

    private String nombreCategoria;

    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaProductoDTO)) {
            return false;
        }

        CategoriaProductoDTO categoriaProductoDTO = (CategoriaProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaProductoDTO{" +
            "id=" + getId() +
            ", nombreCategoria='" + getNombreCategoria() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
