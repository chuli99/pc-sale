package ar.edu.um.programacion2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Caracteristicas;
import ar.edu.um.programacion2.repository.CaracteristicasRepository;
import ar.edu.um.programacion2.service.dto.CaracteristicasDTO;
import ar.edu.um.programacion2.service.mapper.CaracteristicasMapper;
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
 * Integration tests for the {@link CaracteristicasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaracteristicasResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caracteristicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaracteristicasRepository caracteristicasRepository;

    @Autowired
    private CaracteristicasMapper caracteristicasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaracteristicasMockMvc;

    private Caracteristicas caracteristicas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristicas createEntity(EntityManager em) {
        Caracteristicas caracteristicas = new Caracteristicas().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return caracteristicas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caracteristicas createUpdatedEntity(EntityManager em) {
        Caracteristicas caracteristicas = new Caracteristicas().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return caracteristicas;
    }

    @BeforeEach
    public void initTest() {
        caracteristicas = createEntity(em);
    }

    @Test
    @Transactional
    void createCaracteristicas() throws Exception {
        int databaseSizeBeforeCreate = caracteristicasRepository.findAll().size();
        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);
        restCaracteristicasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeCreate + 1);
        Caracteristicas testCaracteristicas = caracteristicasList.get(caracteristicasList.size() - 1);
        assertThat(testCaracteristicas.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCaracteristicas.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCaracteristicasWithExistingId() throws Exception {
        // Create the Caracteristicas with an existing ID
        caracteristicas.setId(1L);
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        int databaseSizeBeforeCreate = caracteristicasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaracteristicasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristicasRepository.findAll().size();
        // set the field null
        caracteristicas.setNombre(null);

        // Create the Caracteristicas, which fails.
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        restCaracteristicasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = caracteristicasRepository.findAll().size();
        // set the field null
        caracteristicas.setDescripcion(null);

        // Create the Caracteristicas, which fails.
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        restCaracteristicasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaracteristicas() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        // Get all the caracteristicasList
        restCaracteristicasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caracteristicas.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCaracteristicas() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        // Get the caracteristicas
        restCaracteristicasMockMvc
            .perform(get(ENTITY_API_URL_ID, caracteristicas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caracteristicas.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCaracteristicas() throws Exception {
        // Get the caracteristicas
        restCaracteristicasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaracteristicas() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();

        // Update the caracteristicas
        Caracteristicas updatedCaracteristicas = caracteristicasRepository.findById(caracteristicas.getId()).get();
        // Disconnect from session so that the updates on updatedCaracteristicas are not directly saved in db
        em.detach(updatedCaracteristicas);
        updatedCaracteristicas.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(updatedCaracteristicas);

        restCaracteristicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristicasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
        Caracteristicas testCaracteristicas = caracteristicasList.get(caracteristicasList.size() - 1);
        assertThat(testCaracteristicas.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCaracteristicas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caracteristicasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaracteristicasWithPatch() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();

        // Update the caracteristicas using partial update
        Caracteristicas partialUpdatedCaracteristicas = new Caracteristicas();
        partialUpdatedCaracteristicas.setId(caracteristicas.getId());

        restCaracteristicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristicas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristicas))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
        Caracteristicas testCaracteristicas = caracteristicasList.get(caracteristicasList.size() - 1);
        assertThat(testCaracteristicas.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCaracteristicas.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCaracteristicasWithPatch() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();

        // Update the caracteristicas using partial update
        Caracteristicas partialUpdatedCaracteristicas = new Caracteristicas();
        partialUpdatedCaracteristicas.setId(caracteristicas.getId());

        partialUpdatedCaracteristicas.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCaracteristicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaracteristicas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaracteristicas))
            )
            .andExpect(status().isOk());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
        Caracteristicas testCaracteristicas = caracteristicasList.get(caracteristicasList.size() - 1);
        assertThat(testCaracteristicas.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCaracteristicas.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caracteristicasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaracteristicas() throws Exception {
        int databaseSizeBeforeUpdate = caracteristicasRepository.findAll().size();
        caracteristicas.setId(count.incrementAndGet());

        // Create the Caracteristicas
        CaracteristicasDTO caracteristicasDTO = caracteristicasMapper.toDto(caracteristicas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaracteristicasMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caracteristicasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caracteristicas in the database
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaracteristicas() throws Exception {
        // Initialize the database
        caracteristicasRepository.saveAndFlush(caracteristicas);

        int databaseSizeBeforeDelete = caracteristicasRepository.findAll().size();

        // Delete the caracteristicas
        restCaracteristicasMockMvc
            .perform(delete(ENTITY_API_URL_ID, caracteristicas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caracteristicas> caracteristicasList = caracteristicasRepository.findAll();
        assertThat(caracteristicasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
