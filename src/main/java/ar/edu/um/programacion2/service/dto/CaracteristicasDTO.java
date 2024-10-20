package ar.edu.um.programacion2.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.programacion2.domain.Caracteristicas} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaracteristicasDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

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
        if (!(o instanceof CaracteristicasDTO)) {
            return false;
        }

        CaracteristicasDTO caracteristicasDTO = (CaracteristicasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caracteristicasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaracteristicasDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", dispositivos=" + getDispositivos() +
            "}";
    }
}
