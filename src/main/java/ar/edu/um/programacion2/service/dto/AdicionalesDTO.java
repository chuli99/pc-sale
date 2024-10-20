package ar.edu.um.programacion2.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.programacion2.domain.Adicionales} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdicionalesDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precio;

    @NotNull
    private BigDecimal precioGratis;

    private DispositivosDTO dispositivos;

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

    public DispositivosDTO getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(DispositivosDTO dispositivos) {
        this.dispositivos = dispositivos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdicionalesDTO)) {
            return false;
        }

        AdicionalesDTO adicionalesDTO = (AdicionalesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adicionalesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdicionalesDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", precioGratis=" + getPrecioGratis() +
            ", dispositivos=" + getDispositivos() +
            "}";
    }
}
