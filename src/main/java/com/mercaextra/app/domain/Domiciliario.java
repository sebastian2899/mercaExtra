package com.mercaextra.app.domain;

import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import com.mercaextra.app.domain.enumeration.TipoSalario;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Domiciliario.
 */
@Entity
@Table(name = "domiciliario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Domiciliario implements Serializable {

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
    @Column(name = "salario")
    private TipoSalario salario;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "horario")
    private String horario;

    @Column(name = "sueldo", precision = 21, scale = 2)
    private BigDecimal sueldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoDomiciliario estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Domiciliario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Domiciliario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Domiciliario apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoSalario getSalario() {
        return this.salario;
    }

    public Domiciliario salario(TipoSalario salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(TipoSalario salario) {
        this.salario = salario;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Domiciliario telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return this.horario;
    }

    public Domiciliario horario(String horario) {
        this.setHorario(horario);
        return this;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public BigDecimal getSueldo() {
        return this.sueldo;
    }

    public Domiciliario sueldo(BigDecimal sueldo) {
        this.setSueldo(sueldo);
        return this;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public EstadoDomiciliario getEstado() {
        return this.estado;
    }

    public Domiciliario estado(EstadoDomiciliario estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoDomiciliario estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domiciliario)) {
            return false;
        }
        return id != null && id.equals(((Domiciliario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Domiciliario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", salario='" + getSalario() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", horario='" + getHorario() + "'" +
            ", sueldo=" + getSueldo() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
