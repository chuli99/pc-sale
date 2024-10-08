package ar.edu.um.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.domain.Personalizacion;
import ar.edu.um.programacion2.repository.PersonalizacionRepository;
import ar.edu.um.programacion2.service.dto.PersonalizacionDTO;
import ar.edu.um.programacion2.service.mapper.PersonalizacionMapper;
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
 * Integration tests for the {@link PersonalizacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalizacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personalizacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalizacionRepository personalizacionRepository;

    @Autowired
    private PersonalizacionMapper personalizacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalizacionMockMvc;

    private Personalizacion personalizacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizacion createEntity(EntityManager em) {
        Personalizacion personalizacion = new Personalizacion().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        personalizacion.setDispositivo(dispositivo);
        return personalizacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizacion createUpdatedEntity(EntityManager em) {
        Personalizacion personalizacion = new Personalizacion().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createUpdatedEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        personalizacion.setDispositivo(dispositivo);
        return personalizacion;
    }

    @BeforeEach
    public void initTest() {
        personalizacion = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalizacion() throws Exception {
        int databaseSizeBeforeCreate = personalizacionRepository.findAll().size();
        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);
        restPersonalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeCreate + 1);
        Personalizacion testPersonalizacion = personalizacionList.get(personalizacionList.size() - 1);
        assertThat(testPersonalizacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersonalizacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createPersonalizacionWithExistingId() throws Exception {
        // Create the Personalizacion with an existing ID
        personalizacion.setId(1L);
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        int databaseSizeBeforeCreate = personalizacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalizacionRepository.findAll().size();
        // set the field null
        personalizacion.setNombre(null);

        // Create the Personalizacion, which fails.
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        restPersonalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalizacionRepository.findAll().size();
        // set the field null
        personalizacion.setDescripcion(null);

        // Create the Personalizacion, which fails.
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        restPersonalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalizacions() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        // Get all the personalizacionList
        restPersonalizacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalizacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getPersonalizacion() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        // Get the personalizacion
        restPersonalizacionMockMvc
            .perform(get(ENTITY_API_URL_ID, personalizacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalizacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingPersonalizacion() throws Exception {
        // Get the personalizacion
        restPersonalizacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalizacion() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();

        // Update the personalizacion
        Personalizacion updatedPersonalizacion = personalizacionRepository.findById(personalizacion.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalizacion are not directly saved in db
        em.detach(updatedPersonalizacion);
        updatedPersonalizacion.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(updatedPersonalizacion);

        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
        Personalizacion testPersonalizacion = personalizacionList.get(personalizacionList.size() - 1);
        assertThat(testPersonalizacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonalizacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalizacionWithPatch() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();

        // Update the personalizacion using partial update
        Personalizacion partialUpdatedPersonalizacion = new Personalizacion();
        partialUpdatedPersonalizacion.setId(personalizacion.getId());

        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
        Personalizacion testPersonalizacion = personalizacionList.get(personalizacionList.size() - 1);
        assertThat(testPersonalizacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersonalizacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdatePersonalizacionWithPatch() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();

        // Update the personalizacion using partial update
        Personalizacion partialUpdatedPersonalizacion = new Personalizacion();
        partialUpdatedPersonalizacion.setId(personalizacion.getId());

        partialUpdatedPersonalizacion.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
        Personalizacion testPersonalizacion = personalizacionList.get(personalizacionList.size() - 1);
        assertThat(testPersonalizacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonalizacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalizacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalizacion() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionRepository.findAll().size();
        personalizacion.setId(count.incrementAndGet());

        // Create the Personalizacion
        PersonalizacionDTO personalizacionDTO = personalizacionMapper.toDto(personalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizacion in the database
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalizacion() throws Exception {
        // Initialize the database
        personalizacionRepository.saveAndFlush(personalizacion);

        int databaseSizeBeforeDelete = personalizacionRepository.findAll().size();

        // Delete the personalizacion
        restPersonalizacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalizacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personalizacion> personalizacionList = personalizacionRepository.findAll();
        assertThat(personalizacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
