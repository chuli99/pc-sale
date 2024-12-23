package ar.edu.um.programacion2.service.mapper;

import ar.edu.um.programacion2.domain.Adicional;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.domain.Personalizacion;
import ar.edu.um.programacion2.domain.Venta;
import ar.edu.um.programacion2.service.dto.AdicionalDTO;
import ar.edu.um.programacion2.service.dto.DispositivoDTO;
import ar.edu.um.programacion2.service.dto.PersonalizacionDTO;
import ar.edu.um.programacion2.service.dto.VentaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    @Mapping(target = "personalizaciones", source = "personalizaciones", qualifiedByName = "personalizacionIdSet")
    @Mapping(target = "adicionales", source = "adicionales", qualifiedByName = "adicionalIdSet")
    VentaDTO toDto(Venta s);

    @Mapping(target = "removePersonalizaciones", ignore = true)
    @Mapping(target = "removeAdicionales", ignore = true)
    Venta toEntity(VentaDTO ventaDTO);

    @Named("dispositivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivoDTO toDtoDispositivoId(Dispositivo dispositivo);

    @Named("personalizacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonalizacionDTO toDtoPersonalizacionId(Personalizacion personalizacion);

    @Named("personalizacionIdSet")
    default Set<PersonalizacionDTO> toDtoPersonalizacionIdSet(Set<Personalizacion> personalizacion) {
        return personalizacion.stream().map(this::toDtoPersonalizacionId).collect(Collectors.toSet());
    }

    @Named("adicionalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdicionalDTO toDtoAdicionalId(Adicional adicional);

    @Named("adicionalIdSet")
    default Set<AdicionalDTO> toDtoAdicionalIdSet(Set<Adicional> adicional) {
        return adicional.stream().map(this::toDtoAdicionalId).collect(Collectors.toSet());
    }
}
