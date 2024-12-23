package ar.edu.um.programacion2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.domain.*;
import ar.edu.um.programacion2.domain.enumeration.Moneda;
import ar.edu.um.programacion2.repository.*;
import ar.edu.um.programacion2.service.dto.DispositivoDTO;
import ar.edu.um.programacion2.service.dto.DispositivoExternoDTO;
import ar.edu.um.programacion2.service.mapper.DispositivoMapper;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class DispositivoServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private CaracteristicaRepository caracteristicaRepository;

    @Mock
    private PersonalizacionRepository personalizacionRepository;

    @Mock
    private OpcionRepository opcionRepository;

    @Mock
    private AdicionalRepository adicionalRepository;

    @Mock
    private DispositivoMapper dispositivoMapper;

    @InjectMocks
    private DispositivoService dispositivoService;

    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpecMock;
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        //Mockear y configurar el WebClient
        webClient = Mockito.mock(WebClient.class);
        requestHeadersUriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.<WebClient.RequestHeadersUriSpec<?>>when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        ReflectionTestUtils.setField(dispositivoService, "webClient", webClient);
    }

    @Test
    void testTraerDispositivos() {
        //Datos para test
        DispositivoExternoDTO dispositivoExternoDTO = new DispositivoExternoDTO();
        dispositivoExternoDTO.setId(1L);
        dispositivoExternoDTO.setCodigo("DISP001");
        dispositivoExternoDTO.setNombre("Dispositivo Test");
        dispositivoExternoDTO.setDescripcion("Descripción de prueba");
        dispositivoExternoDTO.setPrecioBase(BigDecimal.valueOf(100.00));
        dispositivoExternoDTO.setMoneda("USD");
        dispositivoExternoDTO.setCaracteristicas(Collections.emptyList());
        dispositivoExternoDTO.setPersonalizaciones(Collections.emptyList());
        dispositivoExternoDTO.setAdicionales(Collections.emptyList());

        DispositivoExternoDTO[] dispositivosExternoArray = new DispositivoExternoDTO[] { dispositivoExternoDTO };

        when(responseSpecMock.bodyToMono(DispositivoExternoDTO[].class)).thenReturn(Mono.just(dispositivosExternoArray));
        when(dispositivoRepository.findByCodigo(dispositivoExternoDTO.getCodigo())).thenReturn(Optional.empty());
        when(dispositivoRepository.save(any(Dispositivo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(dispositivoMapper.toDto(anyList())).thenAnswer(invocation -> {
            List<Dispositivo> dispositivos = invocation.getArgument(0);
            List<DispositivoDTO> dispositivosDTO = new ArrayList<>();
            for (Dispositivo dispositivo : dispositivos) {
                DispositivoDTO dto = new DispositivoDTO();
                dto.setId(dispositivo.getId());
                dto.setCodigo(dispositivo.getCodigo());
                dto.setNombre(dispositivo.getNombre());
                dto.setDescripcion(dispositivo.getDescripcion());
                dto.setPrecioBase(dispositivo.getPrecioBase());
                dto.setMoneda(dispositivo.getMoneda());
                dispositivosDTO.add(dto);
            }
            return dispositivosDTO;
        });

        List<DispositivoDTO> result = dispositivoService.traerDispositivos();

        //Verificar resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        DispositivoDTO dispositivoDTO = result.get(0);
        assertEquals("DISP001", dispositivoDTO.getCodigo());
        assertEquals("Dispositivo Test", dispositivoDTO.getNombre());
        assertEquals(BigDecimal.valueOf(100.00), dispositivoDTO.getPrecioBase());
        assertEquals(Moneda.USD, dispositivoDTO.getMoneda());

        verify(dispositivoRepository).findByCodigo(dispositivoExternoDTO.getCodigo());
        verify(dispositivoRepository).save(any(Dispositivo.class));
        verify(caracteristicaRepository, never()).save(any(Caracteristica.class));
        verify(personalizacionRepository, never()).save(any(Personalizacion.class));
        verify(opcionRepository, never()).save(any(Opcion.class));
        verify(adicionalRepository, never()).save(any(Adicional.class));
    }
}
