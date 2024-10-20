package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Dispositivos;
import ar.edu.um.programacion2.repository.DispositivosRepository;
import ar.edu.um.programacion2.service.dto.DispositivosDTO;
import ar.edu.um.programacion2.service.mapper.DispositivosMapper;
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
 * Integration tests for the {@link DispositivosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispositivosResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_BASE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_BASE = new BigDecimal(1);

    private static final String DEFAULT_MONEDA = "AAAAAAAAAA";
    private static final String UPDATED_MONEDA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dispositivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispositivosRepository dispositivosRepository;

    @Autowired
    private DispositivosMapper dispositivosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispositivosMockMvc;

    private Dispositivos dispositivos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivos createEntity(EntityManager em) {
        Dispositivos dispositivos = new Dispositivos()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioBase(DEFAULT_PRECIO_BASE)
            .moneda(DEFAULT_MONEDA);
        return dispositivos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivos createUpdatedEntity(EntityManager em) {
        Dispositivos dispositivos = new Dispositivos()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
        return dispositivos;
    }

    @BeforeEach
    public void initTest() {
        dispositivos = createEntity(em);
    }

    @Test
    @Transactional
    void createDispositivos() throws Exception {
        int databaseSizeBeforeCreate = dispositivosRepository.findAll().size();
        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);
        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeCreate + 1);
        Dispositivos testDispositivos = dispositivosList.get(dispositivosList.size() - 1);
        assertThat(testDispositivos.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDispositivos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDispositivos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDispositivos.getPrecioBase()).isEqualByComparingTo(DEFAULT_PRECIO_BASE);
        assertThat(testDispositivos.getMoneda()).isEqualTo(DEFAULT_MONEDA);
    }

    @Test
    @Transactional
    void createDispositivosWithExistingId() throws Exception {
        // Create the Dispositivos with an existing ID
        dispositivos.setId(1L);
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        int databaseSizeBeforeCreate = dispositivosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivosRepository.findAll().size();
        // set the field null
        dispositivos.setCodigo(null);

        // Create the Dispositivos, which fails.
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivosRepository.findAll().size();
        // set the field null
        dispositivos.setNombre(null);

        // Create the Dispositivos, which fails.
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivosRepository.findAll().size();
        // set the field null
        dispositivos.setDescripcion(null);

        // Create the Dispositivos, which fails.
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioBaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivosRepository.findAll().size();
        // set the field null
        dispositivos.setPrecioBase(null);

        // Create the Dispositivos, which fails.
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivosRepository.findAll().size();
        // set the field null
        dispositivos.setMoneda(null);

        // Create the Dispositivos, which fails.
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        restDispositivosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispositivos() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        // Get all the dispositivosList
        restDispositivosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispositivos.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioBase").value(hasItem(sameNumber(DEFAULT_PRECIO_BASE))))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA)));
    }

    @Test
    @Transactional
    void getDispositivos() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        // Get the dispositivos
        restDispositivosMockMvc
            .perform(get(ENTITY_API_URL_ID, dispositivos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispositivos.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioBase").value(sameNumber(DEFAULT_PRECIO_BASE)))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA));
    }

    @Test
    @Transactional
    void getNonExistingDispositivos() throws Exception {
        // Get the dispositivos
        restDispositivosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDispositivos() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();

        // Update the dispositivos
        Dispositivos updatedDispositivos = dispositivosRepository.findById(dispositivos.getId()).get();
        // Disconnect from session so that the updates on updatedDispositivos are not directly saved in db
        em.detach(updatedDispositivos);
        updatedDispositivos
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(updatedDispositivos);

        restDispositivosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
        Dispositivos testDispositivos = dispositivosList.get(dispositivosList.size() - 1);
        assertThat(testDispositivos.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDispositivos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDispositivos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDispositivos.getPrecioBase()).isEqualByComparingTo(UPDATED_PRECIO_BASE);
        assertThat(testDispositivos.getMoneda()).isEqualTo(UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void putNonExistingDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispositivosWithPatch() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();

        // Update the dispositivos using partial update
        Dispositivos partialUpdatedDispositivos = new Dispositivos();
        partialUpdatedDispositivos.setId(dispositivos.getId());

        restDispositivosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivos))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
        Dispositivos testDispositivos = dispositivosList.get(dispositivosList.size() - 1);
        assertThat(testDispositivos.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDispositivos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDispositivos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDispositivos.getPrecioBase()).isEqualByComparingTo(DEFAULT_PRECIO_BASE);
        assertThat(testDispositivos.getMoneda()).isEqualTo(DEFAULT_MONEDA);
    }

    @Test
    @Transactional
    void fullUpdateDispositivosWithPatch() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();

        // Update the dispositivos using partial update
        Dispositivos partialUpdatedDispositivos = new Dispositivos();
        partialUpdatedDispositivos.setId(dispositivos.getId());

        partialUpdatedDispositivos
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);

        restDispositivosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivos))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
        Dispositivos testDispositivos = dispositivosList.get(dispositivosList.size() - 1);
        assertThat(testDispositivos.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDispositivos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDispositivos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDispositivos.getPrecioBase()).isEqualByComparingTo(UPDATED_PRECIO_BASE);
        assertThat(testDispositivos.getMoneda()).isEqualTo(UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void patchNonExistingDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispositivosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispositivos() throws Exception {
        int databaseSizeBeforeUpdate = dispositivosRepository.findAll().size();
        dispositivos.setId(count.incrementAndGet());

        // Create the Dispositivos
        DispositivosDTO dispositivosDTO = dispositivosMapper.toDto(dispositivos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivos in the database
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispositivos() throws Exception {
        // Initialize the database
        dispositivosRepository.saveAndFlush(dispositivos);

        int databaseSizeBeforeDelete = dispositivosRepository.findAll().size();

        // Delete the dispositivos
        restDispositivosMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispositivos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispositivos> dispositivosList = dispositivosRepository.findAll();
        assertThat(dispositivosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
