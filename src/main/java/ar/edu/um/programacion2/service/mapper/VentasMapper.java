package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.domain.Ventas;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import ar.edu.um.programacion2.service.dto.VentasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ventas} and its DTO {@link VentasDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentasMapper extends EntityMapper<VentasDTO, Ventas> {
    @Mapping(target = "idDispositivo", source = "idDispositivo", qualifiedByName = "dispositivosId")
    VentasDTO toDto(Ventas s);

    @Named("dispositivosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivosDTO toDtoDispositivosId(Dispositivos dispositivos);
}
