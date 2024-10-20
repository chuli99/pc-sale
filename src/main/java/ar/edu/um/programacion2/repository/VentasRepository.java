package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Ventas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ventas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {}
