package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Personalizaciones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Personalizaciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalizacionesRepository extends JpaRepository<Personalizaciones, Long> {}
