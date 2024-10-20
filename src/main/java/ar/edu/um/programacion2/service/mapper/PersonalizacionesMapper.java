package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.domain.Personalizaciones;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import ar.edu.um.programacion2.service.dto.PersonalizacionesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personalizaciones} and its DTO {@link PersonalizacionesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalizacionesMapper extends EntityMapper<PersonalizacionesDTO, Personalizaciones> {
    @Mapping(target = "dispositivos", source = "dispositivos", qualifiedByName = "dispositivosId")
    PersonalizacionesDTO toDto(Personalizaciones s);

    @Named("dispositivosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivosDTO toDtoDispositivosId(Dispositivos dispositivos);
}
