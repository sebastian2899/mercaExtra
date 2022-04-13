package com.mercaextra.app.domain;

import com.mercaextra.app.domain.enumeration.TipoDoc;
import com.mercaextra.app.domain.enumeration.TipoProveedor;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proveedor.
 */
@Entity
@Table(name = "proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cc")
    private TipoDoc tipoCC;

    @Column(name = "numero_cc")
    private String numeroCC;

    @Column(name = "num_celular")
    private String numCelular;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proovedor")
    private TipoProveedor tipoProovedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Proveedor apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoDoc getTipoCC() {
        return this.tipoCC;
    }

    public Proveedor tipoCC(TipoDoc tipoCC) {
        this.setTipoCC(tipoCC);
        return this;
    }

    public void setTipoCC(TipoDoc tipoCC) {
        this.tipoCC = tipoCC;
    }

    public String getNumeroCC() {
        return this.numeroCC;
    }

    public Proveedor numeroCC(String numeroCC) {
        this.setNumeroCC(numeroCC);
        return this;
    }

    public void setNumeroCC(String numeroCC) {
        this.numeroCC = numeroCC;
    }

    public String getNumCelular() {
        return this.numCelular;
    }

    public Proveedor numCelular(String numCelular) {
        this.setNumCelular(numCelular);
        return this;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getEmail() {
        return this.email;
    }

    public Proveedor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoProveedor getTipoProovedor() {
        return this.tipoProovedor;
    }

    public Proveedor tipoProovedor(TipoProveedor tipoProovedor) {
        this.setTipoProovedor(tipoProovedor);
        return this;
    }

    public void setTipoProovedor(TipoProveedor tipoProovedor) {
        this.tipoProovedor = tipoProovedor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proveedor)) {
            return false;
        }
        return id != null && id.equals(((Proveedor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedor{" +
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
