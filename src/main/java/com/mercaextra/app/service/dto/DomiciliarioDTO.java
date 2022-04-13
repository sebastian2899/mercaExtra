package com.mercaextra.app.service.dto;

import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import com.mercaextra.app.domain.enumeration.TipoSalario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mercaextra.app.domain.Domiciliario} entity.
 */
public class DomiciliarioDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellido;

    private TipoSalario salario;

    private String telefono;

    private String horario;

    private BigDecimal sueldo;

    private EstadoDomiciliario estado;

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

    public TipoSalario getSalario() {
        return salario;
    }

    public void setSalario(TipoSalario salario) {
        this.salario = salario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public EstadoDomiciliario getEstado() {
        return estado;
    }

    public void setEstado(EstadoDomiciliario estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomiciliarioDTO)) {
            return false;
        }

        DomiciliarioDTO domiciliarioDTO = (DomiciliarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, domiciliarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomiciliarioDTO{" +
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
