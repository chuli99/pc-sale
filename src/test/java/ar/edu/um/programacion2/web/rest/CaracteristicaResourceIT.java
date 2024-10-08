package ar.edu.um.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Caracteristica;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.repository.CaracteristicaRepository;
import ar.edu.um.programacion2.service.dto.CaracteristicaDTO;
import ar.edu.um.programacion2.service.mapper.CaracteristicaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CaracteristicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaracteristicaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caracteristicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private CaracteristicaMapper caracteristicaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaracteristicaMockMvc;

    private Caracteristica caracteristica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristica createEntity(EntityManager em) {
        Caracteristica caracteristica = new Caracteristica().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        caracteristica.setDispositivo(dispositivo);
        return caracteristica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristica createUpdatedEntity(EntityManager em) {
        Caracteristica caracteristica = new Caracteristica().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createUpdatedEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        caracteristica.setDispositivo(dispositivo);
        return caracteristica;
    }

    @BeforeEach
    public void initTest() {
        caracteristica = createEntity(em);
    }

    @Test
    @Transactional
    void createCaracteristica() throws Exception {
        int databaseSizeBeforeCreate = caracteristicaRepository.findAll().size();
        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);
        restCaracteristicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeCreate + 1);
        Caracteristica testCaracteristica = caracteristicaList.get(caracteristicaList.size() - 1);
        assertThat(testCaracteristica.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCaracteristica.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCaracteristicaWithExistingId() throws Exception {
        // Create the Caracteristica with an existing ID
        caracteristica.setId(1L);
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        int databaseSizeBeforeCreate = caracteristicaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaracteristicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristicaRepository.findAll().size();
        // set the field null
        caracteristica.setNombre(null);

        // Create the Caracteristica, which fails.
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        restCaracteristicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristicaRepository.findAll().size();
        // set the field null
        caracteristica.setDescripcion(null);

        // Create the Caracteristica, which fails.
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        restCaracteristicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaracteristicas() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        // Get all the caracteristicaList
        restCaracteristicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristica.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCaracteristica() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        // Get the caracteristica
        restCaracteristicaMockMvc
            .perform(get(ENTITY_API_URL_ID, caracteristica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caracteristica.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCaracteristica() throws Exception {
        // Get the caracteristica
        restCaracteristicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaracteristica() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();

        // Update the caracteristica
        Caracteristica updatedCaracteristica = caracteristicaRepository.findById(caracteristica.getId()).get();
        // Disconnect from session so that the updates on updatedCaracteristica are not directly saved in db
        em.detach(updatedCaracteristica);
        updatedCaracteristica.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(updatedCaracteristica);

        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
        Caracteristica testCaracteristica = caracteristicaList.get(caracteristicaList.size() - 1);
        assertThat(testCaracteristica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCaracteristica.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaracteristicaWithPatch() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();

        // Update the caracteristica using partial update
        Caracteristica partialUpdatedCaracteristica = new Caracteristica();
        partialUpdatedCaracteristica.setId(caracteristica.getId());

        partialUpdatedCaracteristica.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristica))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
        Caracteristica testCaracteristica = caracteristicaList.get(caracteristicaList.size() - 1);
        assertThat(testCaracteristica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCaracteristica.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCaracteristicaWithPatch() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();

        // Update the caracteristica using partial update
        Caracteristica partialUpdatedCaracteristica = new Caracteristica();
        partialUpdatedCaracteristica.setId(caracteristica.getId());

        partialUpdatedCaracteristica.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristica))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
        Caracteristica testCaracteristica = caracteristicaList.get(caracteristicaList.size() - 1);
        assertThat(testCaracteristica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCaracteristica.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caracteristicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaracteristica() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicaRepository.findAll().size();
        caracteristica.setId(count.incrementAndGet());

        // Create the Caracteristica
        CaracteristicaDTO caracteristicaDTO = caracteristicaMapper.toDto(caracteristica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristica in the database
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaracteristica() throws Exception {
        // Initialize the database
        caracteristicaRepository.saveAndFlush(caracteristica);

        int databaseSizeBeforeDelete = caracteristicaRepository.findAll().size();

        // Delete the caracteristica
        restCaracteristicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caracteristica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caracteristica> caracteristicaList = caracteristicaRepository.findAll();
        assertThat(caracteristicaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
