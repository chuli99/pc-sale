package ar.edu.um.programacion2.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ar.edu.um.programacion2.domain.Ventas} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentasDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precioFinal;

    @NotNull
    private Instant fechaVenta;

    private DispositivosDTO idDispositivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public DispositivosDTO getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(DispositivosDTO idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentasDTO)) {
            return false;
        }

        VentasDTO ventasDTO = (VentasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentasDTO{" +
            "id=" + getId() +
            ", precioFinal=" + getPrecioFinal() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", idDispositivo=" + getIdDispositivo() +
            "}";
    }
}
