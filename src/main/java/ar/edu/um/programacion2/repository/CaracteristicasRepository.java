package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Caracteristicas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Caracteristicas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaracteristicasRepository extends JpaRepository<Caracteristicas, Long> {}
