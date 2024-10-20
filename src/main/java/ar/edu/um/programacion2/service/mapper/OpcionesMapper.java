package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Opciones;
import ar.edu.um.programacion2.domain.Personalizaciones;
import ar.edu.um.programacion2.service.dto.OpcionesDTO;
import ar.edu.um.programacion2.service.dto.PersonalizacionesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Opciones} and its DTO {@link OpcionesDTO}.
 */
@Mapper(componentModel = "spring")
public interface OpcionesMapper extends EntityMapper<OpcionesDTO, Opciones> {
    @Mapping(target = "personalizaciones", source = "personalizaciones", qualifiedByName = "personalizacionesId")
    OpcionesDTO toDto(Opciones s);

    @Named("personalizacionesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonalizacionesDTO toDtoPersonalizacionesId(Personalizaciones personalizaciones);
}
