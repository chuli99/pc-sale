package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Adicionales;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Adicionales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdicionalesRepository extends JpaRepository<Adicionales, Long> {}
