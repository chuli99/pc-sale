package ar.edu.um.programacion2.web.rest;

import ar.edu.um.programacion2.repository.OpcionesRepository;
import ar.edu.um.programacion2.service.OpcionesService;
import ar.edu.um.programacion2.service.dto.OpcionesDTO;
import ar.edu.um.programacion2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.domain.Opciones}.
 */
@RestController
@RequestMapping("/api")
public class OpcionesResource {

    private final Logger log = LoggerFactory.getLogger(OpcionesResource.class);

    private static final String ENTITY_NAME = "opciones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpcionesService opcionesService;

    private final OpcionesRepository opcionesRepository;

    public OpcionesResource(OpcionesService opcionesService, OpcionesRepository opcionesRepository) {
        this.opcionesService = opcionesService;
        this.opcionesRepository = opcionesRepository;
    }

    /**
     * {@code POST  /opciones} : Create a new opciones.
     *
     * @param opcionesDTO the opcionesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opcionesDTO, or with status {@code 400 (Bad Request)} if the opciones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opciones")
    public ResponseEntity<OpcionesDTO> createOpciones(@Valid @RequestBody OpcionesDTO opcionesDTO) throws URISyntaxException {
        log.debug("REST request to save Opciones : {}", opcionesDTO);
        if (opcionesDTO.getId() != null) {
            throw new BadRequestAlertException("A new opciones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpcionesDTO result = opcionesService.save(opcionesDTO);
        return ResponseEntity
            .created(new URI("/api/opciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /opciones/:id} : Updates an existing opciones.
     *
     * @param id the id of the opcionesDTO to save.
     * @param opcionesDTO the opcionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opcionesDTO,
     * or with status {@code 400 (Bad Request)} if the opcionesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opcionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opciones/{id}")
    public ResponseEntity<OpcionesDTO> updateOpciones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OpcionesDTO opcionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Opciones : {}, {}", id, opcionesDTO);
        if (opcionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opcionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opcionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OpcionesDTO result = opcionesService.update(opcionesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opcionesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /opciones/:id} : Partial updates given fields of an existing opciones, field will ignore if it is null
     *
     * @param id the id of the opcionesDTO to save.
     * @param opcionesDTO the opcionesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opcionesDTO,
     * or with status {@code 400 (Bad Request)} if the opcionesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the opcionesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the opcionesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/opciones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OpcionesDTO> partialUpdateOpciones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OpcionesDTO opcionesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Opciones partially : {}, {}", id, opcionesDTO);
        if (opcionesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opcionesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opcionesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OpcionesDTO> result = opcionesService.partialUpdate(opcionesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opcionesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /opciones} : get all the opciones.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opciones in body.
     */
    @GetMapping("/opciones")
    public ResponseEntity<List<OpcionesDTO>> getAllOpciones(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Opciones");
        Page<OpcionesDTO> page = opcionesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /opciones/:id} : get the "id" opciones.
     *
     * @param id the id of the opcionesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opcionesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opciones/{id}")
    public ResponseEntity<OpcionesDTO> getOpciones(@PathVariable Long id) {
        log.debug("REST request to get Opciones : {}", id);
        Optional<OpcionesDTO> opcionesDTO = opcionesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opcionesDTO);
    }

    /**
     * {@code DELETE  /opciones/:id} : delete the "id" opciones.
     *
     * @param id the id of the opcionesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/opciones/{id}")
    public ResponseEntity<Void> deleteOpciones(@PathVariable Long id) {
        log.debug("REST request to delete Opciones : {}", id);
        opcionesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
