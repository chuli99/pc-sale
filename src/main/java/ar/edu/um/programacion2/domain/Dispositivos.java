package ar.edu.um.programacion2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dispositivos.
 */
@Entity
@Table(name = "dispositivos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dispositivos implements Serializable {

    private static final long serialVersionUID = 1L;

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_base", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioBase;

    @NotNull
    @Column(name = "moneda", nullable = false)
    private String moneda;

    @OneToMany(mappedBy = "dispositivos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dispositivos" }, allowSetters = true)
    private Set<Caracteristicas> caracteristicas = new HashSet<>();

    @OneToMany(mappedBy = "dispositivos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dispositivos" }, allowSetters = true)
    private Set<Adicionales> adicionales = new HashSet<>();

    @OneToMany(mappedBy = "dispositivos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opciones", "dispositivos" }, allowSetters = true)
    private Set<Personalizaciones> personalizaciones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dispositivos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Dispositivos codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Dispositivos nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Dispositivos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioBase() {
        return this.precioBase;
    }

    public Dispositivos precioBase(BigDecimal precioBase) {
        this.setPrecioBase(precioBase);
        return this;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public String getMoneda() {
        return this.moneda;
    }

    public Dispositivos moneda(String moneda) {
        this.setMoneda(moneda);
        return this;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Set<Caracteristicas> getCaracteristicas() {
        return this.caracteristicas;
    }

    public void setCaracteristicas(Set<Caracteristicas> caracteristicas) {
        if (this.caracteristicas != null) {
            this.caracteristicas.forEach(i -> i.setDispositivos(null));
        }
        if (caracteristicas != null) {
            caracteristicas.forEach(i -> i.setDispositivos(this));
        }
        this.caracteristicas = caracteristicas;
    }

    public Dispositivos caracteristicas(Set<Caracteristicas> caracteristicas) {
        this.setCaracteristicas(caracteristicas);
        return this;
    }

    public Dispositivos addCaracteristicas(Caracteristicas caracteristicas) {
        this.caracteristicas.add(caracteristicas);
        caracteristicas.setDispositivos(this);
        return this;
    }

    public Dispositivos removeCaracteristicas(Caracteristicas caracteristicas) {
        this.caracteristicas.remove(caracteristicas);
        caracteristicas.setDispositivos(null);
        return this;
    }

    public Set<Adicionales> getAdicionales() {
        return this.adicionales;
    }

    public void setAdicionales(Set<Adicionales> adicionales) {
        if (this.adicionales != null) {
            this.adicionales.forEach(i -> i.setDispositivos(null));
        }
        if (adicionales != null) {
            adicionales.forEach(i -> i.setDispositivos(this));
        }
        this.adicionales = adicionales;
    }

    public Dispositivos adicionales(Set<Adicionales> adicionales) {
        this.setAdicionales(adicionales);
        return this;
    }

    public Dispositivos addAdicionales(Adicionales adicionales) {
        this.adicionales.add(adicionales);
        adicionales.setDispositivos(this);
        return this;
    }

    public Dispositivos removeAdicionales(Adicionales adicionales) {
        this.adicionales.remove(adicionales);
        adicionales.setDispositivos(null);
        return this;
    }

    public Set<Personalizaciones> getPersonalizaciones() {
        return this.personalizaciones;
    }

    public void setPersonalizaciones(Set<Personalizaciones> personalizaciones) {
        if (this.personalizaciones != null) {
            this.personalizaciones.forEach(i -> i.setDispositivos(null));
        }
        if (personalizaciones != null) {
            personalizaciones.forEach(i -> i.setDispositivos(this));
        }
        this.personalizaciones = personalizaciones;
    }

    public Dispositivos personalizaciones(Set<Personalizaciones> personalizaciones) {
        this.setPersonalizaciones(personalizaciones);
        return this;
    }

    public Dispositivos addPersonalizaciones(Personalizaciones personalizaciones) {
        this.personalizaciones.add(personalizaciones);
        personalizaciones.setDispositivos(this);
        return this;
    }

    public Dispositivos removePersonalizaciones(Personalizaciones personalizaciones) {
        this.personalizaciones.remove(personalizaciones);
        personalizaciones.setDispositivos(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dispositivos)) {
            return false;
        }
        return id != null && id.equals(((Dispositivos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dispositivos{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioBase=" + getPrecioBase() +
            ", moneda='" + getMoneda() + "'" +
            "}";
    }
}
