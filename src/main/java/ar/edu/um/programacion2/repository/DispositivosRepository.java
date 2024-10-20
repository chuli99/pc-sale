package ar.edu.um.programacion2.repository;

import ar.edu.um.programacion2.domain.Dispositivos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dispositivos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispositivosRepository extends JpaRepository<Dispositivos, Long> {}
