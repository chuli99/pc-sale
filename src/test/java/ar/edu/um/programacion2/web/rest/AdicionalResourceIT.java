package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Adicional;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.repository.AdicionalRepository;
import ar.edu.um.programacion2.service.dto.AdicionalDTO;
import ar.edu.um.programacion2.service.mapper.AdicionalMapper;
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
 * Integration tests for the {@link AdicionalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdicionalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRECIO_GRATIS = new BigDecimal(-1);
    private static final BigDecimal UPDATED_PRECIO_GRATIS = new BigDecimal(0);

    private static final String ENTITY_API_URL = "/api/adicionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private AdicionalMapper adicionalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdicionalMockMvc;

    private Adicional adicional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createEntity(EntityManager em) {
        Adicional adicional = new Adicional()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .precioGratis(DEFAULT_PRECIO_GRATIS);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        adicional.setDispositivo(dispositivo);
        return adicional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createUpdatedEntity(EntityManager em) {
        Adicional adicional = new Adicional()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createUpdatedEntity(em);
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        adicional.setDispositivo(dispositivo);
        return adicional;
    }

    @BeforeEach
    public void initTest() {
        adicional = createEntity(em);
    }

    @Test
    @Transactional
    void createAdicional() throws Exception {
        int databaseSizeBeforeCreate = adicionalRepository.findAll().size();
        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);
        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isCreated());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeCreate + 1);
        Adicional testAdicional = adicionalList.get(adicionalList.size() - 1);
        assertThat(testAdicional.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAdicional.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAdicional.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testAdicional.getPrecioGratis()).isEqualByComparingTo(DEFAULT_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void createAdicionalWithExistingId() throws Exception {
        // Create the Adicional with an existing ID
        adicional.setId(1L);
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        int databaseSizeBeforeCreate = adicionalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalRepository.findAll().size();
        // set the field null
        adicional.setNombre(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalRepository.findAll().size();
        // set the field null
        adicional.setDescripcion(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalRepository.findAll().size();
        // set the field null
        adicional.setPrecio(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioGratisIsRequired() throws Exception {
        int databaseSizeBeforeTest = adicionalRepository.findAll().size();
        // set the field null
        adicional.setPrecioGratis(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdicionals() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        // Get all the adicionalList
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adicional.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].precioGratis").value(hasItem(sameNumber(DEFAULT_PRECIO_GRATIS))));
    }

    @Test
    @Transactional
    void getAdicional() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        // Get the adicional
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL_ID, adicional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adicional.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.precioGratis").value(sameNumber(DEFAULT_PRECIO_GRATIS)));
    }

    @Test
    @Transactional
    void getNonExistingAdicional() throws Exception {
        // Get the adicional
        restAdicionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdicional() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();

        // Update the adicional
        Adicional updatedAdicional = adicionalRepository.findById(adicional.getId()).get();
        // Disconnect from session so that the updates on updatedAdicional are not directly saved in db
        em.detach(updatedAdicional);
        updatedAdicional.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).precio(UPDATED_PRECIO).precioGratis(UPDATED_PRECIO_GRATIS);
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(updatedAdicional);

        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
        Adicional testAdicional = adicionalList.get(adicionalList.size() - 1);
        assertThat(testAdicional.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicional.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAdicional.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testAdicional.getPrecioGratis()).isEqualByComparingTo(UPDATED_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void putNonExistingAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adicionalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional.nombre(UPDATED_NOMBRE);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
        Adicional testAdicional = adicionalList.get(adicionalList.size() - 1);
        assertThat(testAdicional.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicional.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAdicional.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testAdicional.getPrecioGratis()).isEqualByComparingTo(DEFAULT_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void fullUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
        Adicional testAdicional = adicionalList.get(adicionalList.size() - 1);
        assertThat(testAdicional.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAdicional.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAdicional.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testAdicional.getPrecioGratis()).isEqualByComparingTo(UPDATED_PRECIO_GRATIS);
    }

    @Test
    @Transactional
    void patchNonExistingAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdicional() throws Exception {
        int databaseSizeBeforeUpdate = adicionalRepository.findAll().size();
        adicional.setId(count.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adicionalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdicional() throws Exception {
        // Initialize the database
        adicionalRepository.saveAndFlush(adicional);

        int databaseSizeBeforeDelete = adicionalRepository.findAll().size();

        // Delete the adicional
        restAdicionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, adicional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adicional> adicionalList = adicionalRepository.findAll();
        assertThat(adicionalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
