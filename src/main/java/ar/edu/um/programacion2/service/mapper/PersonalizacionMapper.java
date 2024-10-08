package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.domain.Personalizacion;
import ar.edu.um.programacion2.service.dto.DispositivoDTO;
import ar.edu.um.programacion2.service.dto.PersonalizacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personalizacion} and its DTO {@link PersonalizacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalizacionMapper extends EntityMapper<PersonalizacionDTO, Personalizacion> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    PersonalizacionDTO toDto(Personalizacion s);

    @Named("dispositivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivoDTO toDtoDispositivoId(Dispositivo dispositivo);
}
