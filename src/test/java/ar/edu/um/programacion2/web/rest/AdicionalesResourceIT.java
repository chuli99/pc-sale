package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Adicionales;
import ar.edu.um.programacion2.repository.AdicionalesRepository;
import ar.edu.um.programacion2.service.dto.AdicionalesDTO;
import ar.edu.um.programacion2.service.mapper.AdicionalesMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link AdicionalesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdicionalesResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRECIO_GRATIS = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_GRATIS = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/adicionales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdicionalesRepository adicionalesRepository;

    @Autowired
    private AdicionalesMapper adicionalesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdicionalesMockMvc;

    private Adicionales adicionales;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicionales createEntity(EntityManager em) {
        Adicionales adicionales = new Adicionales()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .precioGratis(DEFAULT_PRECIO_GRATIS);
        return adicionales;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicionales createUpdatedEntity(EntityManager em) {
        Adicionales adicionales = new Adicionales()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        return adicionales;
    }

    @BeforeEach
    public void initTest() {
        adicionales = createEntity(em);
    }

    @Test
    @Transactional
    void createAdicionales() throws Exception {
        int databaseSizeBeforeCreate = adicionalesRepository.findAll().size();
        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);
        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeCreate + 1);
        Adicionales testAdicionales = adicionalesList.get(adicionalesList.size() - 1);
        assertThat(testAdicionales.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAdicionales.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAdicionales.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testAdicionales.getPrecioGratis()).isEqualByComparingTo(DEFAULT_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void createAdicionalesWithExistingId() throws Exception {
        // Create the Adicionales with an existing ID
        adicionales.setId(1L);
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        int databaseSizeBeforeCreate = adicionalesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalesRepository.findAll().size();
        // set the field null
        adicionales.setNombre(null);

        // Create the Adicionales, which fails.
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalesRepository.findAll().size();
        // set the field null
        adicionales.setDescripcion(null);

        // Create the Adicionales, which fails.
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalesRepository.findAll().size();
        // set the field null
        adicionales.setPrecio(null);

        // Create the Adicionales, which fails.
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioGratisIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalesRepository.findAll().size();
        // set the field null
        adicionales.setPrecioGratis(null);

        // Create the Adicionales, which fails.
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        restAdicionalesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdicionales() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        // Get all the adicionalesList
        restAdicionalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adicionales.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].precioGratis").value(hasItem(sameNumber(DEFAULT_PRECIO_GRATIS))));
    }

    @Test
    @Transactional
    void getAdicionales() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        // Get the adicionales
        restAdicionalesMockMvc
            .perform(get(ENTITY_API_URL_ID, adicionales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adicionales.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.precioGratis").value(sameNumber(DEFAULT_PRECIO_GRATIS)));
    }

    @Test
    @Transactional
    void getNonExistingAdicionales() throws Exception {
        // Get the adicionales
        restAdicionalesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdicionales() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();

        // Update the adicionales
        Adicionales updatedAdicionales = adicionalesRepository.findById(adicionales.getId()).get();
        // Disconnect from session so that the updates on updatedAdicionales are not directly saved in db
        em.detach(updatedAdicionales);
        updatedAdicionales
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(updatedAdicionales);

        restAdicionalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
        Adicionales testAdicionales = adicionalesList.get(adicionalesList.size() - 1);
        assertThat(testAdicionales.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicionales.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAdicionales.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testAdicionales.getPrecioGratis()).isEqualByComparingTo(UPDATED_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void putNonExistingAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdicionalesWithPatch() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();

        // Update the adicionales using partial update
        Adicionales partialUpdatedAdicionales = new Adicionales();
        partialUpdatedAdicionales.setId(adicionales.getId());

        partialUpdatedAdicionales.nombre(UPDATED_NOMBRE);

        restAdicionalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicionales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdicionales))
            )
            .andExpect(status().isOk());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
        Adicionales testAdicionales = adicionalesList.get(adicionalesList.size() - 1);
        assertThat(testAdicionales.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicionales.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAdicionales.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testAdicionales.getPrecioGratis()).isEqualByComparingTo(DEFAULT_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void fullUpdateAdicionalesWithPatch() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();

        // Update the adicionales using partial update
        Adicionales partialUpdatedAdicionales = new Adicionales();
        partialUpdatedAdicionales.setId(adicionales.getId());

        partialUpdatedAdicionales
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicionales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdicionales))
            )
            .andExpect(status().isOk());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
        Adicionales testAdicionales = adicionalesList.get(adicionalesList.size() - 1);
        assertThat(testAdicionales.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicionales.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAdicionales.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testAdicionales.getPrecioGratis()).isEqualByComparingTo(UPDATED_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void patchNonExistingAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adicionalesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdicionales() throws Exception {
        int databaseSizeBeforeUpdate = adicionalesRepository.findAll().size();
        adicionales.setId(count.incrementAndGet());

        // Create the Adicionales
        AdicionalesDTO adicionalesDTO = adicionalesMapper.toDto(adicionales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adicionalesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicionales in the database
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdicionales() throws Exception {
        // Initialize the database
        adicionalesRepository.saveAndFlush(adicionales);

        int databaseSizeBeforeDelete = adicionalesRepository.findAll().size();

        // Delete the adicionales
        restAdicionalesMockMvc
            .perform(delete(ENTITY_API_URL_ID, adicionales.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adicionales> adicionalesList = adicionalesRepository.findAll();
        assertThat(adicionalesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
