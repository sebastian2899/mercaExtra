package com.mercaextra.app.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "email")
    private String email;

    @Column(name = "num_celular")
    private String numCelular;

    @Column(name = "direccion")
    private Instant direccion;

    @Column(name = "salario", precision = 21, scale = 2)
    private BigDecimal salario;

    @Column(name = "horario")
    private String horario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empleado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Empleado apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return this.email;
    }

    public Empleado email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumCelular() {
        return this.numCelular;
    }

    public Empleado numCelular(String numCelular) {
        this.setNumCelular(numCelular);
        return this;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public Instant getDireccion() {
        return this.direccion;
    }

    public Empleado direccion(Instant direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(Instant direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getSalario() {
        return this.salario;
    }

    public Empleado salario(BigDecimal salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getHorario() {
        return this.horario;
    }

    public Empleado horario(String horario) {
        this.setHorario(horario);
        return this;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return id != null && id.equals(((Empleado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", email='" + getEmail() + "'" +
            ", numCelular='" + getNumCelular() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", salario=" + getSalario() +
            ", horario='" + getHorario() + "'" +
            "}";
    }
}
