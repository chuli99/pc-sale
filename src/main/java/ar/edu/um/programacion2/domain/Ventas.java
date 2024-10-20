package ar.edu.um.programacion2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ventas.
 */
@Entity
@Table(name = "ventas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ventas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_final", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioFinal;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "caracteristicas", "adicionales", "personalizaciones" }, allowSetters = true)
    private Dispositivos idDispositivo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ventas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecioFinal() {
        return this.precioFinal;
    }

    public Ventas precioFinal(BigDecimal precioFinal) {
        this.setPrecioFinal(precioFinal);
        return this;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Instant getFechaVenta() {
        return this.fechaVenta;
    }

    public Ventas fechaVenta(Instant fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Dispositivos getIdDispositivo() {
        return this.idDispositivo;
    }

    public void setIdDispositivo(Dispositivos dispositivos) {
        this.idDispositivo = dispositivos;
    }

    public Ventas idDispositivo(Dispositivos dispositivos) {
        this.setIdDispositivo(dispositivos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ventas)) {
            return false;
        }
        return id != null && id.equals(((Ventas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ventas{" +
            "id=" + getId() +
            ", precioFinal=" + getPrecioFinal() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            "}";
    }
}
