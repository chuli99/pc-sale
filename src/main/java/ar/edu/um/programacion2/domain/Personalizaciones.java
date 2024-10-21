package ar.edu.um.programacion2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personalizaciones.
 */
@Entity
@Table(name = "personalizaciones")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personalizaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "personalizaciones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personalizaciones" }, allowSetters = true)
    private Set<Opciones> opciones = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "caracteristicas", "adicionales", "personalizaciones" }, allowSetters = true)
    private Dispositivos dispositivos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personalizaciones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Personalizaciones nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Personalizaciones descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Opciones> getOpciones() {
        return this.opciones;
    }

    public void setOpciones(Set<Opciones> opciones) {
        if (this.opciones != null) {
            this.opciones.forEach(i -> i.setPersonalizaciones(null));
        }
        if (opciones != null) {
            opciones.forEach(i -> i.setPersonalizaciones(this));
        }
        this.opciones = opciones;
    }

    public Personalizaciones opciones(Set<Opciones> opciones) {
        this.setOpciones(opciones);
        return this;
    }

    public Personalizaciones addOpciones(Opciones opciones) {
        this.opciones.add(opciones);
        opciones.setPersonalizaciones(this);
        return this;
    }

    public Personalizaciones removeOpciones(Opciones opciones) {
        this.opciones.remove(opciones);
        opciones.setPersonalizaciones(null);
        return this;
    }

    public Dispositivos getDispositivos() {
        return this.dispositivos;
    }

    public void setDispositivos(Dispositivos dispositivos) {
        this.dispositivos = dispositivos;
    }

    public Personalizaciones dispositivos(Dispositivos dispositivos) {
        this.setDispositivos(dispositivos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personalizaciones)) {
            return false;
        }
        return id != null && id.equals(((Personalizaciones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personalizaciones{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
