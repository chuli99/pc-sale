package ar.edu.um.programacion2.web.rest;

import static ar.edu.um.programacion2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.IntegrationTest;
import ar.edu.um.programacion2.domain.Dispositivo;
import ar.edu.um.programacion2.domain.enumeration.Moneda;
import ar.edu.um.programacion2.repository.DispositivoRepository;
import ar.edu.um.programacion2.service.dto.DispositivoDTO;
import ar.edu.um.programacion2.service.mapper.DispositivoMapper;
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
 * Integration tests for the {@link DispositivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispositivoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_BASE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_BASE = new BigDecimal(1);

    private static final Moneda DEFAULT_MONEDA = Moneda.ARS;
    private static final Moneda UPDATED_MONEDA = Moneda.USD;

    private static final String ENTITY_API_URL = "/api/dispositivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private DispositivoMapper dispositivoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispositivoMockMvc;

    private Dispositivo dispositivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createEntity(EntityManager em) {
        Dispositivo dispositivo = new Dispositivo()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioBase(DEFAULT_PRECIO_BASE)
            .moneda(DEFAULT_MONEDA);
        return dispositivo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createUpdatedEntity(EntityManager em) {
        Dispositivo dispositivo = new Dispositivo()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
        return dispositivo;
    }

    @BeforeEach
    public void initTest() {
        dispositivo = createEntity(em);
    }

    @Test
    @Transactional
    void createDispositivo() throws Exception {
        int databaseSizeBeforeCreate = dispositivoRepository.findAll().size();
        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);
        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeCreate + 1);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDispositivo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDispositivo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDispositivo.getPrecioBase()).isEqualByComparingTo(DEFAULT_PRECIO_BASE);
        assertThat(testDispositivo.getMoneda()).isEqualTo(DEFAULT_MONEDA);
    }

    @Test
    @Transactional
    void createDispositivoWithExistingId() throws Exception {
        // Create the Dispositivo with an existing ID
        dispositivo.setId(1L);
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        int databaseSizeBeforeCreate = dispositivoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivoRepository.findAll().size();
        // set the field null
        dispositivo.setCodigo(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivoRepository.findAll().size();
        // set the field null
        dispositivo.setNombre(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivoRepository.findAll().size();
        // set the field null
        dispositivo.setDescripcion(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioBaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivoRepository.findAll().size();
        // set the field null
        dispositivo.setPrecioBase(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispositivoRepository.findAll().size();
        // set the field null
        dispositivo.setMoneda(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispositivos() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispositivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioBase").value(hasItem(sameNumber(DEFAULT_PRECIO_BASE))))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())));
    }

    @Test
    @Transactional
    void getDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get the dispositivo
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL_ID, dispositivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispositivo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioBase").value(sameNumber(DEFAULT_PRECIO_BASE)))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDispositivo() throws Exception {
        // Get the dispositivo
        restDispositivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo
        Dispositivo updatedDispositivo = dispositivoRepository.findById(dispositivo.getId()).get();
        // Disconnect from session so that the updates on updatedDispositivo are not directly saved in db
        em.detach(updatedDispositivo);
        updatedDispositivo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(updatedDispositivo);

        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDispositivo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDispositivo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDispositivo.getPrecioBase()).isEqualByComparingTo(UPDATED_PRECIO_BASE);
        assertThat(testDispositivo.getMoneda()).isEqualTo(UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void putNonExistingDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispositivoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo.descripcion(UPDATED_DESCRIPCION).moneda(UPDATED_MONEDA);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testDispositivo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDispositivo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDispositivo.getPrecioBase()).isEqualByComparingTo(DEFAULT_PRECIO_BASE);
        assertThat(testDispositivo.getMoneda()).isEqualTo(UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void fullUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testDispositivo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDispositivo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDispositivo.getPrecioBase()).isEqualByComparingTo(UPDATED_PRECIO_BASE);
        assertThat(testDispositivo.getMoneda()).isEqualTo(UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void patchNonExistingDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dispositivoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeDelete = dispositivoRepository.findAll().size();

        // Delete the dispositivo
        restDispositivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispositivo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
