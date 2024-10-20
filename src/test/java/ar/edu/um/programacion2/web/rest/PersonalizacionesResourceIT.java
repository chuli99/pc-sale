package ar.edu.um.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Personalizaciones;
import ar.edu.um.programacion2.repository.PersonalizacionesRepository;
import ar.edu.um.programacion2.service.dto.PersonalizacionesDTO;
import ar.edu.um.programacion2.service.mapper.PersonalizacionesMapper;
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
 * Integration tests for the {@link PersonalizacionesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalizacionesResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personalizaciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalizacionesRepository personalizacionesRepository;

    @Autowired
    private PersonalizacionesMapper personalizacionesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalizacionesMockMvc;

    private Personalizaciones personalizaciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizaciones createEntity(EntityManager em) {
        Personalizaciones personalizaciones = new Personalizaciones().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return personalizaciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personalizaciones createUpdatedEntity(EntityManager em) {
        Personalizaciones personalizaciones = new Personalizaciones().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return personalizaciones;
    }

    @BeforeEach
    public void initTest() {
        personalizaciones = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalizaciones() throws Exception {
        int databaseSizeBeforeCreate = personalizacionesRepository.findAll().size();
        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);
        restPersonalizacionesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeCreate + 1);
        Personalizaciones testPersonalizaciones = personalizacionesList.get(personalizacionesList.size() - 1);
        assertThat(testPersonalizaciones.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersonalizaciones.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createPersonalizacionesWithExistingId() throws Exception {
        // Create the Personalizaciones with an existing ID
        personalizaciones.setId(1L);
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        int databaseSizeBeforeCreate = personalizacionesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalizacionesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalizacionesRepository.findAll().size();
        // set the field null
        personalizaciones.setNombre(null);

        // Create the Personalizaciones, which fails.
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        restPersonalizacionesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalizacionesRepository.findAll().size();
        // set the field null
        personalizaciones.setDescripcion(null);

        // Create the Personalizaciones, which fails.
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        restPersonalizacionesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalizaciones() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        // Get all the personalizacionesList
        restPersonalizacionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalizaciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getPersonalizaciones() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        // Get the personalizaciones
        restPersonalizacionesMockMvc
            .perform(get(ENTITY_API_URL_ID, personalizaciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalizaciones.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingPersonalizaciones() throws Exception {
        // Get the personalizaciones
        restPersonalizacionesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalizaciones() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();

        // Update the personalizaciones
        Personalizaciones updatedPersonalizaciones = personalizacionesRepository.findById(personalizaciones.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalizaciones are not directly saved in db
        em.detach(updatedPersonalizaciones);
        updatedPersonalizaciones.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(updatedPersonalizaciones);

        restPersonalizacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizacionesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
        Personalizaciones testPersonalizaciones = personalizacionesList.get(personalizacionesList.size() - 1);
        assertThat(testPersonalizaciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonalizaciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizacionesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalizacionesWithPatch() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();

        // Update the personalizaciones using partial update
        Personalizaciones partialUpdatedPersonalizaciones = new Personalizaciones();
        partialUpdatedPersonalizaciones.setId(personalizaciones.getId());

        partialUpdatedPersonalizaciones.nombre(UPDATED_NOMBRE);

        restPersonalizacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizaciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizaciones))
            )
            .andExpect(status().isOk());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
        Personalizaciones testPersonalizaciones = personalizacionesList.get(personalizacionesList.size() - 1);
        assertThat(testPersonalizaciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonalizaciones.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdatePersonalizacionesWithPatch() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();

        // Update the personalizaciones using partial update
        Personalizaciones partialUpdatedPersonalizaciones = new Personalizaciones();
        partialUpdatedPersonalizaciones.setId(personalizaciones.getId());

        partialUpdatedPersonalizaciones.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restPersonalizacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizaciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizaciones))
            )
            .andExpect(status().isOk());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
        Personalizaciones testPersonalizaciones = personalizacionesList.get(personalizacionesList.size() - 1);
        assertThat(testPersonalizaciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonalizaciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalizacionesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalizaciones() throws Exception {
        int databaseSizeBeforeUpdate = personalizacionesRepository.findAll().size();
        personalizaciones.setId(count.incrementAndGet());

        // Create the Personalizaciones
        PersonalizacionesDTO personalizacionesDTO = personalizacionesMapper.toDto(personalizaciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizacionesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizacionesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personalizaciones in the database
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalizaciones() throws Exception {
        // Initialize the database
        personalizacionesRepository.saveAndFlush(personalizaciones);

        int databaseSizeBeforeDelete = personalizacionesRepository.findAll().size();

        // Delete the personalizaciones
        restPersonalizacionesMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalizaciones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personalizaciones> personalizacionesList = personalizacionesRepository.findAll();
        assertThat(personalizacionesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
