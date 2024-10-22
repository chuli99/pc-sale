package ar.edu.um.programacion2.service;

import ar.edu.um.programacion2.domain.*;
import ar.edu.um.programacion2.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostEntitiesService {

    private final Logger log = LoggerFactory.getLogger(PostEntitiesService.class);
    private final DispositivosRepository dispositivoRepository;
    private final CaracteristicasRepository caracteristicaRepository;
    private final PersonalizacionesRepository personalizacionRepository;
    private final OpcionesRepository opcionRepository;
    private final AdicionalesRepository adicionalRepository;
    private final JSONDeserializer jsonDeserializer;
    private final ObjectMapper mapper = new ObjectMapper();

    public PostEntitiesService(
        DispositivosRepository dispositivoRepository,
        CaracteristicasRepository caracteristicaRepository,
        PersonalizacionesRepository personalizacionRepository,
        OpcionesRepository opcionRepository,
        AdicionalesRepository adicionalRepository,
        JSONDeserializer jsonDeserializer
    ) {
        this.dispositivoRepository = dispositivoRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.opcionRepository = opcionRepository;
        this.adicionalRepository = adicionalRepository;
        this.jsonDeserializer = jsonDeserializer;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        log.info("Iniciado sincrozacion de datos al iniciar");
        postEntities();
    }

    // Método para enviar el JSON mediante POST al servidor
    @Scheduled(fixedRate = 21600000)
    public void postEntities() {
        log.info("Posteando datos localmente a localhost:8080");
        try {
            postDispositivo();
            postCaracteristica();
            postPersonalizacion();
            postOpcion();
            postAdicional();
            log.info("Finalizamos posteo");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error al postear datos");
        }
    }

    private void postDispositivo() throws IOException {
        List<Dispositivos> dispositivos = parseDispositivos();
        log.info("Posteando Dispositivo");
        for (Dispositivos dispositivo : dispositivos) {
            if (!dispositivoRepository.existsById(dispositivo.getId())) {
                dispositivoRepository.save(dispositivo);
            }
        }
    }

    private void postCaracteristica() throws IOException {
        List<Caracteristicas> caracteristicas = parseCaracteristicas();
        log.info("Posteando Caracteristica");
        for (Caracteristicas caracteristica : caracteristicas) {
            if (!caracteristicaRepository.existsById(caracteristica.getId())) {
                caracteristicaRepository.save(caracteristica);
            }
        }
    }

    private void postPersonalizacion() throws IOException {
        List<Personalizaciones> personalizaciones = parsePersonalizaciones();
        log.info("Posteando Personalizacion");
        for (Personalizaciones personalizacion : personalizaciones) {
            if (!personalizacionRepository.existsById(personalizacion.getId())) {
                personalizacionRepository.save(personalizacion);
            }
        }
    }

    private void postOpcion() throws IOException {
        List<Opciones> opciones = parseOpciones();
        log.info("Posteando opcion");
        for (Opciones opcion : opciones) {
            if (!opcionRepository.existsById(opcion.getId())) {
                opcionRepository.save(opcion);
            }
        }
    }

    private void postAdicional() throws IOException {
        List<Adicionales> adicionales = parseAdicionales();
        log.info("Posteando Adicional");
        for (Adicionales adicional : adicionales) {
            if (!adicionalRepository.existsById(adicional.getId())) {
                adicionalRepository.save(adicional);
            }
        }
    }

    private List<Dispositivos> parseDispositivos() throws IOException {
        log.info("Parseando Dispositivos");
        String dispositivosJSON = JSONDeserializer.dispositivosFromJson();
        log.info(dispositivosJSON);
        return mapper.readValue(dispositivosJSON, new TypeReference<List<Dispositivos>>() {});
    }

    private List<Caracteristicas> parseCaracteristicas() throws IOException {
        log.info("Parseando Caracteristicas");
        String caracteristicasJSON = JSONDeserializer.caracteristicasFromJson();
        return mapper.readValue(caracteristicasJSON, new TypeReference<List<Caracteristicas>>() {});
    }

    private List<Personalizaciones> parsePersonalizaciones() throws IOException {
        log.info("Parseando Personalizaciones");
        String personalizacionesJSON = JSONDeserializer.personalizacionesFromJson();
        return mapper.readValue(personalizacionesJSON, new TypeReference<List<Personalizaciones>>() {});
    }

    private List<Opciones> parseOpciones() throws IOException {
        log.info("Parseando Opciones");
        String opcionesJSON = JSONDeserializer.opcionesFromJson();
        return mapper.readValue(opcionesJSON, new TypeReference<List<Opciones>>() {});
    }

    private List<Adicionales> parseAdicionales() throws IOException {
        log.info("Parseando Adicionales");
        String adicionalesJSON = JSONDeserializer.adicionalesFromJson();
        return mapper.readValue(adicionalesJSON, new TypeReference<List<Adicionales>>() {});
    }
}
