package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Opcion;
import ar.edu.um.programacion2.domain.Personalizacion;
import ar.edu.um.programacion2.repository.OpcionRepository;
import ar.edu.um.programacion2.service.dto.OpcionDTO;
import ar.edu.um.programacion2.service.mapper.OpcionMapper;
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
 * Integration tests for the {@link OpcionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpcionResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_ADICIONAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_ADICIONAL = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/opcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private OpcionMapper opcionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpcionMockMvc;

    private Opcion opcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opcion createEntity(EntityManager em) {
        Opcion opcion = new Opcion()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioAdicional(DEFAULT_PRECIO_ADICIONAL);
        // Add required entity
        Personalizacion personalizacion;
        if (TestUtil.findAll(em, Personalizacion.class).isEmpty()) {
            personalizacion = PersonalizacionResourceIT.createEntity(em);
            em.persist(personalizacion);
            em.flush();
        } else {
            personalizacion = TestUtil.findAll(em, Personalizacion.class).get(0);
        }
        opcion.setPersonalizacion(personalizacion);
        return opcion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opcion createUpdatedEntity(EntityManager em) {
        Opcion opcion = new Opcion()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);
        // Add required entity
        Personalizacion personalizacion;
        if (TestUtil.findAll(em, Personalizacion.class).isEmpty()) {
            personalizacion = PersonalizacionResourceIT.createUpdatedEntity(em);
            em.persist(personalizacion);
            em.flush();
        } else {
            personalizacion = TestUtil.findAll(em, Personalizacion.class).get(0);
        }
        opcion.setPersonalizacion(personalizacion);
        return opcion;
    }

    @BeforeEach
    public void initTest() {
        opcion = createEntity(em);
    }

    @Test
    @Transactional
    void createOpcion() throws Exception {
        int databaseSizeBeforeCreate = opcionRepository.findAll().size();
        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);
        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isCreated());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeCreate + 1);
        Opcion testOpcion = opcionList.get(opcionList.size() - 1);
        assertThat(testOpcion.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOpcion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testOpcion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOpcion.getPrecioAdicional()).isEqualByComparingTo(DEFAULT_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void createOpcionWithExistingId() throws Exception {
        // Create the Opcion with an existing ID
        opcion.setId(1L);
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        int databaseSizeBeforeCreate = opcionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRepository.findAll().size();
        // set the field null
        opcion.setCodigo(null);

        // Create the Opcion, which fails.
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isBadRequest());

        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRepository.findAll().size();
        // set the field null
        opcion.setNombre(null);

        // Create the Opcion, which fails.
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isBadRequest());

        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRepository.findAll().size();
        // set the field null
        opcion.setDescripcion(null);

        // Create the Opcion, which fails.
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isBadRequest());

        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioAdicionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRepository.findAll().size();
        // set the field null
        opcion.setPrecioAdicional(null);

        // Create the Opcion, which fails.
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        restOpcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isBadRequest());

        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpcions() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        // Get all the opcionList
        restOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioAdicional").value(hasItem(sameNumber(DEFAULT_PRECIO_ADICIONAL))));
    }

    @Test
    @Transactional
    void getOpcion() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        // Get the opcion
        restOpcionMockMvc
            .perform(get(ENTITY_API_URL_ID, opcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opcion.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioAdicional").value(sameNumber(DEFAULT_PRECIO_ADICIONAL)));
    }

    @Test
    @Transactional
    void getNonExistingOpcion() throws Exception {
        // Get the opcion
        restOpcionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOpcion() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();

        // Update the opcion
        Opcion updatedOpcion = opcionRepository.findById(opcion.getId()).get();
        // Disconnect from session so that the updates on updatedOpcion are not directly saved in db
        em.detach(updatedOpcion);
        updatedOpcion
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);
        OpcionDTO opcionDTO = opcionMapper.toDto(updatedOpcion);

        restOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
        Opcion testOpcion = opcionList.get(opcionList.size() - 1);
        assertThat(testOpcion.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpcion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOpcion.getPrecioAdicional()).isEqualByComparingTo(UPDATED_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void putNonExistingOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpcionWithPatch() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();

        // Update the opcion using partial update
        Opcion partialUpdatedOpcion = new Opcion();
        partialUpdatedOpcion.setId(opcion.getId());

        partialUpdatedOpcion.nombre(UPDATED_NOMBRE);

        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcion))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
        Opcion testOpcion = opcionList.get(opcionList.size() - 1);
        assertThat(testOpcion.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpcion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOpcion.getPrecioAdicional()).isEqualByComparingTo(DEFAULT_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void fullUpdateOpcionWithPatch() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();

        // Update the opcion using partial update
        Opcion partialUpdatedOpcion = new Opcion();
        partialUpdatedOpcion.setId(opcion.getId());

        partialUpdatedOpcion
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);

        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcion))
            )
            .andExpect(status().isOk());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
        Opcion testOpcion = opcionList.get(opcionList.size() - 1);
        assertThat(testOpcion.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpcion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOpcion.getPrecioAdicional()).isEqualByComparingTo(UPDATED_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void patchNonExistingOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opcionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpcion() throws Exception {
        int databaseSizeBeforeUpdate = opcionRepository.findAll().size();
        opcion.setId(count.incrementAndGet());

        // Create the Opcion
        OpcionDTO opcionDTO = opcionMapper.toDto(opcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(opcionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opcion in the database
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpcion() throws Exception {
        // Initialize the database
        opcionRepository.saveAndFlush(opcion);

        int databaseSizeBeforeDelete = opcionRepository.findAll().size();

        // Delete the opcion
        restOpcionMockMvc
            .perform(delete(ENTITY_API_URL_ID, opcion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opcion> opcionList = opcionRepository.findAll();
        assertThat(opcionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
