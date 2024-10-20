package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Adicionales;
import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.service.dto.AdicionalesDTO;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adicionales} and its DTO {@link AdicionalesDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdicionalesMapper extends EntityMapper<AdicionalesDTO, Adicionales> {
    @Mapping(target = "dispositivos", source = "dispositivos", qualifiedByName = "dispositivosId")
    AdicionalesDTO toDto(Adicionales s);

    @Named("dispositivosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivosDTO toDtoDispositivosId(Dispositivos dispositivos);
}
