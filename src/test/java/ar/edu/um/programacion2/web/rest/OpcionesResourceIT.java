package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Opciones;
import ar.edu.um.programacion2.repository.OpcionesRepository;
import ar.edu.um.programacion2.service.dto.OpcionesDTO;
import ar.edu.um.programacion2.service.mapper.OpcionesMapper;
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
 * Integration tests for the {@link OpcionesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpcionesResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_ADICIONAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_ADICIONAL = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/opciones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpcionesRepository opcionesRepository;

    @Autowired
    private OpcionesMapper opcionesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpcionesMockMvc;

    private Opciones opciones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opciones createEntity(EntityManager em) {
        Opciones opciones = new Opciones()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioAdicional(DEFAULT_PRECIO_ADICIONAL);
        return opciones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opciones createUpdatedEntity(EntityManager em) {
        Opciones opciones = new Opciones()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);
        return opciones;
    }

    @BeforeEach
    public void initTest() {
        opciones = createEntity(em);
    }

    @Test
    @Transactional
    void createOpciones() throws Exception {
        int databaseSizeBeforeCreate = opcionesRepository.findAll().size();
        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);
        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isCreated());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeCreate + 1);
        Opciones testOpciones = opcionesList.get(opcionesList.size() - 1);
        assertThat(testOpciones.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOpciones.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testOpciones.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOpciones.getPrecioAdicional()).isEqualByComparingTo(DEFAULT_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void createOpcionesWithExistingId() throws Exception {
        // Create the Opciones with an existing ID
        opciones.setId(1L);
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        int databaseSizeBeforeCreate = opcionesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionesRepository.findAll().size();
        // set the field null
        opciones.setCodigo(null);

        // Create the Opciones, which fails.
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isBadRequest());

        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionesRepository.findAll().size();
        // set the field null
        opciones.setNombre(null);

        // Create the Opciones, which fails.
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isBadRequest());

        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionesRepository.findAll().size();
        // set the field null
        opciones.setDescripcion(null);

        // Create the Opciones, which fails.
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isBadRequest());

        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioAdicionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionesRepository.findAll().size();
        // set the field null
        opciones.setPrecioAdicional(null);

        // Create the Opciones, which fails.
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        restOpcionesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isBadRequest());

        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpciones() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        // Get all the opcionesList
        restOpcionesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioAdicional").value(hasItem(sameNumber(DEFAULT_PRECIO_ADICIONAL))));
    }

    @Test
    @Transactional
    void getOpciones() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        // Get the opciones
        restOpcionesMockMvc
            .perform(get(ENTITY_API_URL_ID, opciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opciones.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioAdicional").value(sameNumber(DEFAULT_PRECIO_ADICIONAL)));
    }

    @Test
    @Transactional
    void getNonExistingOpciones() throws Exception {
        // Get the opciones
        restOpcionesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOpciones() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();

        // Update the opciones
        Opciones updatedOpciones = opcionesRepository.findById(opciones.getId()).get();
        // Disconnect from session so that the updates on updatedOpciones are not directly saved in db
        em.detach(updatedOpciones);
        updatedOpciones
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(updatedOpciones);

        restOpcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcionesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
        Opciones testOpciones = opcionesList.get(opcionesList.size() - 1);
        assertThat(testOpciones.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOpciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOpciones.getPrecioAdicional()).isEqualByComparingTo(UPDATED_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void putNonExistingOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcionesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpcionesWithPatch() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();

        // Update the opciones using partial update
        Opciones partialUpdatedOpciones = new Opciones();
        partialUpdatedOpciones.setId(opciones.getId());

        partialUpdatedOpciones.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).precioAdicional(UPDATED_PRECIO_ADICIONAL);

        restOpcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpciones))
            )
            .andExpect(status().isOk());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
        Opciones testOpciones = opcionesList.get(opcionesList.size() - 1);
        assertThat(testOpciones.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOpciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOpciones.getPrecioAdicional()).isEqualByComparingTo(UPDATED_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void fullUpdateOpcionesWithPatch() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();

        // Update the opciones using partial update
        Opciones partialUpdatedOpciones = new Opciones();
        partialUpdatedOpciones.setId(opciones.getId());

        partialUpdatedOpciones
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioAdicional(UPDATED_PRECIO_ADICIONAL);

        restOpcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpciones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpciones))
            )
            .andExpect(status().isOk());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
        Opciones testOpciones = opcionesList.get(opcionesList.size() - 1);
        assertThat(testOpciones.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOpciones.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testOpciones.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOpciones.getPrecioAdicional()).isEqualByComparingTo(UPDATED_PRECIO_ADICIONAL);
    }

    @Test
    @Transactional
    void patchNonExistingOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opcionesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpciones() throws Exception {
        int databaseSizeBeforeUpdate = opcionesRepository.findAll().size();
        opciones.setId(count.incrementAndGet());

        // Create the Opciones
        OpcionesDTO opcionesDTO = opcionesMapper.toDto(opciones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(opcionesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opciones in the database
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpciones() throws Exception {
        // Initialize the database
        opcionesRepository.saveAndFlush(opciones);

        int databaseSizeBeforeDelete = opcionesRepository.findAll().size();

        // Delete the opciones
        restOpcionesMockMvc
            .perform(delete(ENTITY_API_URL_ID, opciones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opciones> opcionesList = opcionesRepository.findAll();
        assertThat(opcionesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
