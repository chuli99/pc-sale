package ar.edu.um.programacion2.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.domain.Venta;
import ar.edu.um.programacion2.repository.*;
import ar.edu.um.programacion2.service.dto.VentaCatedraDTO;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
class VentaTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private PersonalizacionRepository personalizacionRepository;

    @Mock
    private OpcionRepository opcionRepository;

    @Mock
    private AdicionalRepository adicionalRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private VentaService ventaService;

    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ventaService, "baseUrl", "http://mockurl.com");
        ReflectionTestUtils.setField(ventaService, "apiToken", "mock-token");
    }

    private void configurarWebClientMock() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder);

        WebClient webClientMock = Mockito.mock(WebClient.class);
        when(webClientBuilder.build()).thenReturn(webClientMock);

        requestBodyUriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        Mockito.<WebClient.RequestHeadersSpec<?>>when(requestBodySpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("Mock Response"));

        ventaService.initWebClient();
    }

    @Test
    void testGuardarVenta() {
        configurarWebClientMock();

        VentaCatedraDTO request = new VentaCatedraDTO();
        request.setIdDispositivo(1L);

        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setId(1L);
        dispositivo.setPrecioBase(BigDecimal.valueOf(100));

        when(dispositivoRepository.findById(1L)).thenReturn(Optional.of(dispositivo));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Método guardar
        Venta result = ventaService.guardarVenta(request);

        //Validar resultados
        Assertions.assertNotNull(result);
        Assertions.assertEquals(dispositivo, result.getDispositivo());
        Assertions.assertEquals(BigDecimal.valueOf(100), result.getPrecioFinal());

        //Verificar interacciones
        verify(dispositivoRepository, times(1)).findById(1L);
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    void testPostearVentaCatedra() {
        configurarWebClientMock();

        Venta venta = new Venta();
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setIdExterno(1L);
        venta.setDispositivo(dispositivo);
        venta.setPrecioFinal(BigDecimal.valueOf(150));
        venta.setFechaVenta(ZonedDateTime.now());

        ventaService.postearVentaCatedra(venta);

        verify(requestBodyUriSpecMock, times(1)).uri("/vender");
    }
}
