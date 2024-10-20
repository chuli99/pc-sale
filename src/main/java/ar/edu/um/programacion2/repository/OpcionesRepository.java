package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Opciones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Opciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpcionesRepository extends JpaRepository<Opciones, Long> {}
