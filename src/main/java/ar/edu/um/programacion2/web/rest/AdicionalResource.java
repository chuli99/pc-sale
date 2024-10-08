package ar.edu.um.programacion2.web.rest;

import ar.edu.um.programacion2.repository.AdicionalRepository;
import ar.edu.um.programacion2.service.AdicionalService;
import ar.edu.um.programacion2.service.dto.AdicionalDTO;
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
 * REST controller for managing {@link ar.edu.um.programacion2.domain.Adicional}.
 */
@RestController
@RequestMapping("/api")
public class AdicionalResource {

    private final Logger log = LoggerFactory.getLogger(AdicionalResource.class);

    private static final String ENTITY_NAME = "adicional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdicionalService adicionalService;

    private final AdicionalRepository adicionalRepository;

    public AdicionalResource(AdicionalService adicionalService, AdicionalRepository adicionalRepository) {
        this.adicionalService = adicionalService;
        this.adicionalRepository = adicionalRepository;
    }

    /**
     * {@code POST  /adicionals} : Create a new adicional.
     *
     * @param adicionalDTO the adicionalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adicionalDTO, or with status {@code 400 (Bad Request)} if the adicional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adicionals")
    public ResponseEntity<AdicionalDTO> createAdicional(@Valid @RequestBody AdicionalDTO adicionalDTO) throws URISyntaxException {
        log.debug("REST request to save Adicional : {}", adicionalDTO);
        if (adicionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new adicional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdicionalDTO result = adicionalService.save(adicionalDTO);
        return ResponseEntity
            .created(new URI("/api/adicionals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adicionals/:id} : Updates an existing adicional.
     *
     * @param id the id of the adicionalDTO to save.
     * @param adicionalDTO the adicionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adicionalDTO,
     * or with status {@code 400 (Bad Request)} if the adicionalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adicionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adicionals/{id}")
    public ResponseEntity<AdicionalDTO> updateAdicional(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdicionalDTO adicionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Adicional : {}, {}", id, adicionalDTO);
        if (adicionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adicionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adicionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdicionalDTO result = adicionalService.update(adicionalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adicionalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /adicionals/:id} : Partial updates given fields of an existing adicional, field will ignore if it is null
     *
     * @param id the id of the adicionalDTO to save.
     * @param adicionalDTO the adicionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adicionalDTO,
     * or with status {@code 400 (Bad Request)} if the adicionalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adicionalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adicionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adicionals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdicionalDTO> partialUpdateAdicional(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdicionalDTO adicionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Adicional partially : {}, {}", id, adicionalDTO);
        if (adicionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adicionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adicionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdicionalDTO> result = adicionalService.partialUpdate(adicionalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adicionalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /adicionals} : get all the adicionals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adicionals in body.
     */
    @GetMapping("/adicionals")
    public ResponseEntity<List<AdicionalDTO>> getAllAdicionals(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Adicionals");
        Page<AdicionalDTO> page = adicionalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adicionals/:id} : get the "id" adicional.
     *
     * @param id the id of the adicionalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adicionalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adicionals/{id}")
    public ResponseEntity<AdicionalDTO> getAdicional(@PathVariable Long id) {
        log.debug("REST request to get Adicional : {}", id);
        Optional<AdicionalDTO> adicionalDTO = adicionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adicionalDTO);
    }

    /**
     * {@code DELETE  /adicionals/:id} : delete the "id" adicional.
     *
     * @param id the id of the adicionalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adicionals/{id}")
    public ResponseEntity<Void> deleteAdicional(@PathVariable Long id) {
        log.debug("REST request to delete Adicional : {}", id);
        adicionalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
