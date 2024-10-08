package ar.edu.um.programacion2.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.programacion2.domain.Adicional} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdicionalDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precio;

    @NotNull
    @DecimalMin(value = "-1")
    private BigDecimal precioGratis;

    private DispositivoDTO dispositivo;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioGratis() {
        return precioGratis;
    }

    public void setPrecioGratis(BigDecimal precioGratis) {
        this.precioGratis = precioGratis;
    }

    public DispositivoDTO getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(DispositivoDTO dispositivo) {
        this.dispositivo = dispositivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdicionalDTO)) {
            return false;
        }

        AdicionalDTO adicionalDTO = (AdicionalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adicionalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdicionalDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", precioGratis=" + getPrecioGratis() +
            ", dispositivo=" + getDispositivo() +
            "}";
    }
}
