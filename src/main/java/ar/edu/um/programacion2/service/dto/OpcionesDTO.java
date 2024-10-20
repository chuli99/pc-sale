package ar.edu.um.programacion2.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.programacion2.domain.Opciones} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OpcionesDTO implements Serializable {

    private Long id;

    @NotNull
    private String codigo;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precioAdicional;

    private PersonalizacionesDTO personalizaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioAdicional() {
        return precioAdicional;
    }

    public void setPrecioAdicional(BigDecimal precioAdicional) {
        this.precioAdicional = precioAdicional;
    }

    public PersonalizacionesDTO getPersonalizaciones() {
        return personalizaciones;
    }

    public void setPersonalizaciones(PersonalizacionesDTO personalizaciones) {
        this.personalizaciones = personalizaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpcionesDTO)) {
            return false;
        }

        OpcionesDTO opcionesDTO = (OpcionesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, opcionesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpcionesDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioAdicional=" + getPrecioAdicional() +
            ", personalizaciones=" + getPersonalizaciones() +
            "}";
    }
}
