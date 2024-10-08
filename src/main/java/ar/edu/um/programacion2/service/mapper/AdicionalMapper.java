package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Adicional;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.service.dto.AdicionalDTO;
import ar.edu.um.programacion2.service.dto.DispositivoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adicional} and its DTO {@link AdicionalDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdicionalMapper extends EntityMapper<AdicionalDTO, Adicional> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    AdicionalDTO toDto(Adicional s);

    @Named("dispositivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivoDTO toDtoDispositivoId(Dispositivo dispositivo);
}
