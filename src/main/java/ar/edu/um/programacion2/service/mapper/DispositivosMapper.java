package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dispositivos} and its DTO {@link DispositivosDTO}.
 */
@Mapper(componentModel = "spring")
public interface DispositivosMapper extends EntityMapper<DispositivosDTO, Dispositivos> {}
