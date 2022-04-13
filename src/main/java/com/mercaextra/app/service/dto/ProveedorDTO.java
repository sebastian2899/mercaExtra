package com.mercaextra.app.service.dto;

import com.mercaextra.app.domain.enumeration.TipoDoc;
import com.mercaextra.app.domain.enumeration.TipoProveedor;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Proveedor} entity.
 */
public class ProveedorDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellido;

    private TipoDoc tipoCC;

    private String numeroCC;

    private String numCelular;

    private String email;

    private TipoProveedor tipoProovedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoDoc getTipoCC() {
        return tipoCC;
    }

    public void setTipoCC(TipoDoc tipoCC) {
        this.tipoCC = tipoCC;
    }

    public String getNumeroCC() {
        return numeroCC;
    }

    public void setNumeroCC(String numeroCC) {
        this.numeroCC = numeroCC;
    }

    public String getNumCelular() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoProveedor getTipoProovedor() {
        return tipoProovedor;
    }

    public void setTipoProovedor(TipoProveedor tipoProovedor) {
        this.tipoProovedor = tipoProovedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProveedorDTO)) {
            return false;
        }

        ProveedorDTO proveedorDTO = (ProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", tipoCC='" + getTipoCC() + "'" +
            ", numeroCC='" + getNumeroCC() + "'" +
            ", numCelular='" + getNumCelular() + "'" +
            ", email='" + getEmail() + "'" +
            ", tipoProovedor='" + getTipoProovedor() + "'" +
            "}";
    }
}
