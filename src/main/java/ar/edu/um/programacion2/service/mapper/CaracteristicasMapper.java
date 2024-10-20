package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Caracteristicas;
import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.service.dto.CaracteristicasDTO;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caracteristicas} and its DTO {@link CaracteristicasDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaracteristicasMapper extends EntityMapper<CaracteristicasDTO, Caracteristicas> {
    @Mapping(target = "dispositivos", source = "dispositivos", qualifiedByName = "dispositivosId")
    CaracteristicasDTO toDto(Caracteristicas s);

    @Named("dispositivosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivosDTO toDtoDispositivosId(Dispositivos dispositivos);
}
